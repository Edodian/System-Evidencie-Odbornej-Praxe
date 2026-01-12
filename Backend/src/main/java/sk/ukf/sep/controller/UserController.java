package sk.ukf.sep.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import sk.ukf.sep.entity.User;
import sk.ukf.sep.repository.UserRepository;
import sk.ukf.sep.service.EmailService;
import sk.ukf.sep.service.KeycloakOAuthService;
import sk.ukf.sep.util.PasswordUtil;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
//@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController {

    private final UserRepository repository;
    private final EmailService emailService;
    private final KeycloakOAuthService keycloakOAuthService;

    private boolean hasFinalPassword(User u) {
        return u.getPwd() != null && !u.getPwd().isBlank();
    }

    private boolean hasTempPassword(User u) {
        return u.getTempPwd() != null && !u.getTempPwd().isBlank();
    }

    private boolean requiresPasswordCreation(User u) {
        return !hasFinalPassword(u) && hasTempPassword(u);
    }

    private boolean isTempPasswordStillValid(User u) {
        LocalDateTime updatedAt = u.getUpdatedAt();
        if (updatedAt == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return !updatedAt.isBefore(now.minusMinutes(5));
    }

    private String resolveEmailFromJwt(Jwt jwt) {
        if (jwt == null) return null;

        String email = jwt.getClaimAsString("email");
        if (email != null && !email.isBlank()) return email;

        String preferred = jwt.getClaimAsString("preferred_username");
        if (preferred != null && !preferred.isBlank()) return preferred;

        // fallback for older local tokens
        return jwt.getSubject();
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerStudent(@RequestBody User student) {
        if (student == null || student.getEmail() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Missing or invalid email."));
        }

        if (!student.getEmail().endsWith("@student.ukf.sk")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Only student email addresses ending with @student.ukf.sk are allowed."));
        }

        student.setPwd("");

        User saved = repository.save(student);

        // tempPwd sends
        try {
            emailService.sendTemporaryPassword(
                    saved.getEmail(),
                    saved.getName() + " " + saved.getSurname(),
                    saved.getTempPwd()
            );
        } catch (Exception e) {
            System.err.println("Failed to send registration email: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "id", saved.getId(),
                        "email", saved.getEmail()
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        String password = req.get("password");

        System.out.println("DEBUG /login email='" + email + "', password='" + password + "'");

        User u = repository.findByEmail(email);
        System.out.println("DEBUG user from DB = " + u);

        if (u == null) {
            System.out.println("DEBUG -> user not found, returning 401");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password."));
        }

        if (requiresPasswordCreation(u)) {
            System.out.println("DEBUG requiresPasswordCreation=true, returning 403");
            String newTemp = PasswordUtil.generate(8);
            u.setTempPwd(newTemp);
            repository.save(u);

            try {
                emailService.sendTemporaryPassword(
                        u.getEmail(),
                        u.getName() + " " + u.getSurname(),
                        newTemp
                );
            } catch (Exception e) {
                System.err.println("Failed to resend temporary password: " + e.getMessage());
            }

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "status", "PASSWORD_CREATION_REQUIRED",
                            "message", "Temporary password was sent to your email. Use it to create a new password.",
                            "email", u.getEmail()
                    ));
        }

        // Keep existing DB password check (unchanged behavior)
        if (!hasFinalPassword(u) || !u.getPwd().equals(password)) {
            System.out.println("DEBUG password mismatch or no final pwd, returning 401");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password."));
        }

        // cleaning temppwd just in case
        if (hasTempPassword(u)) {
            u.setTempPwd("");
            repository.save(u);
        }

        // OAuth2 Direct Grant via Keycloak (backend only; frontend never talks to Keycloak)
        try {
            Map<String, Object> token;
            try {
                token = keycloakOAuthService.passwordGrant(email, password);
            } catch (Exception firstTry) {
                String role = (u.getRole() == null) ? "STUDENT" : u.getRole().toString();
                keycloakOAuthService.upsertUserWithPasswordAndRole(email, password, role);
                token = keycloakOAuthService.passwordGrant(email, password);
            }

            return ResponseEntity.ok(Map.of(
                    "access_token", token.get("access_token"),
                    "token_type", token.getOrDefault("token_type", "Bearer"),
                    "expires_in", token.getOrDefault("expires_in", 0)
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "Authentication service unavailable."));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout() {
        return ResponseEntity.ok(Map.of("message", "Logged out successfully (token discarded on client)."));
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile(
            @AuthenticationPrincipal Jwt jwt
    ) {
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "You must be logged in."));
        }

        String email = resolveEmailFromJwt(jwt);

        User u = repository.findByEmail(email);
        if (u == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found."));
        }

        if (requiresPasswordCreation(u)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Create your password first using the temporary code sent to your email."));
        }

        return ResponseEntity.ok(Map.of(
                "id", u.getId(),
                "email", u.getEmail(),
                "name", u.getName(),
                "surname", u.getSurname(),
                "role", u.getRole(),
                "altEmail", u.getAltEmail(),
                "phone", u.getPhone(),
                "field", u.getField()
        ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> requestReset(@RequestBody Map<String, String> body) {
        String email = body.get("email");

        User u = repository.findByEmail(email);
        if (u == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found."));
        }

        String resetCode = PasswordUtil.generate(8);

        u.setTempPwd(resetCode);
        repository.save(u);

        try {
            emailService.sendPasswordResetCode(
                    u.getEmail(),
                    u.getName() + " " + u.getSurname(),
                    resetCode
            );
        } catch (Exception e) {
            System.err.println("Failed to send reset email: " + e.getMessage());
        }

        return ResponseEntity.ok(Map.of(
                "message", "Reset code was sent to your email and is valid for 5 minutes."
        ));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(
            @RequestBody Map<String, String> req,
            @AuthenticationPrincipal Jwt jwt
    ) {
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "You must be logged in."));
        }

        String oldPassword = req.get("oldPassword");
        String newPassword = req.get("newPassword");

        if (oldPassword == null || newPassword == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Missing required fields."));
        }

        String email = resolveEmailFromJwt(jwt);
        User u = repository.findByEmail(email);

        if (u == null || !hasFinalPassword(u) || !u.getPwd().equals(oldPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials."));
        }

        u.setPwd(newPassword);

        if (hasTempPassword(u)) {
            u.setTempPwd("");
        }

        repository.save(u);

        return ResponseEntity.ok(Map.of(
                "status", "PASSWORD_CHANGED",
                "message", "Password successfully changed."
        ));
    }

    @PostMapping("/create-password")
    public ResponseEntity<Map<String, Object>> createPassword(
            @RequestBody Map<String, String> req
    ) {
        String email = req.get("email");
        String tempPassword = req.get("tempPassword");
        String newPassword = req.get("newPassword");
        String confirmPassword = req.get("confirmPassword");

        if (email == null || tempPassword == null || newPassword == null || confirmPassword == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Missing required fields."));
        }

        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "New password and confirmation do not match."));
        }

        if (newPassword.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "New password cannot be empty."));
        }

        User u = repository.findByEmail(email);
        if (u == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found."));
        }

        if (!hasTempPassword(u)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "No active temporary password for this account."));
        }

        if (!u.getTempPwd().equals(tempPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid temporary password."));
        }

        u.setPwd(newPassword);
        u.setTempPwd("");
        repository.save(u);

        return ResponseEntity.ok(Map.of(
                "status", "PASSWORD_CREATED",
                "message", "Password created successfully.",
                "email", u.getEmail()
        ));
    }

    @PostMapping("/verify-temp-password")
    public ResponseEntity<Map<String, Object>> verifyTempPassword(@RequestBody Map<String, String> req) {

        String email = req.get("email");
        String tempPassword = req.get("tempPassword");

        if (email == null || tempPassword == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Missing required fields."));
        }

        User u = repository.findByEmail(email);
        if (u == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found."));
        }

        if (!hasTempPassword(u)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "No active temporary password for this account."));
        }

        if (!isTempPasswordStillValid(u)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "status", "TEMPORARY_PASSWORD_EXPIRED",
                            "message", "Temporary password has expired. Please request a new reset code."
                    ));
        }

        if (!u.getTempPwd().equals(tempPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid temporary password."));
        }

        return ResponseEntity.ok(Map.of(
                "status", "OK",
                "message", "Temporary password is valid."
        ));
    }
}
