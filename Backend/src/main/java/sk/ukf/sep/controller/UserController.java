package sk.ukf.sep.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import sk.ukf.sep.entity.User;
import sk.ukf.sep.repository.UserRepository;
import sk.ukf.sep.service.EmailService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository repository;
    private final EmailService emailService;

    @PostMapping("/student")
    public ResponseEntity<Map<String, Object>> registerStudent(@RequestBody User student) {
        if (student == null || student.getEmail() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Missing or invalid email."));
        }

        if (!student.getEmail().endsWith("@student.ukf.sk")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Only student email addresses ending with @student.ukf.sk are allowed."));
        }

        student.setPwd(null); // trigger @PrePersist
        User saved = repository.save(student);

        // send temp password via mailpit
        try {
            emailService.sendTemporaryPassword(
                    saved.getEmail(),
                    saved.getName() + " " + saved.getSurname(),
                    saved.getPwd()
            );
        } catch (Exception e) {
            System.err.println("Failed to send registration email: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "id", saved.getId(),
                        "email", saved.getEmail(),
                        "pwd", saved.getPwd()
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        String password = req.get("password");

        User u = repository.findByEmail(email);
        if (u == null || u.getPwd() == null || !u.getPwd().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password."));
        }

        if (u.isMustChangePwd()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "status", "PASSWORD_CHANGE_REQUIRED",
                            "message", "You must change your temporary password before accessing the system."
                    ));
        }

        return ResponseEntity.ok(Map.of(
                "status", "OK",
                "userId", u.getId()
        ));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        String oldPassword = req.get("oldPassword");
        String newPassword = req.get("newPassword");

        User u = repository.findByEmail(email);
        if (u == null || u.getPwd() == null || !u.getPwd().equals(oldPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials."));
        }

        u.setPwd(newPassword);
        u.setMustChangePwd(false);
        repository.save(u);

        return ResponseEntity.ok(Map.of(
                "status", "PASSWORD_CHANGED",
                "message", "Password successfully changed. You can now log in normally."
        ));
    }

    // this endpoint requires password to be changed first
    @GetMapping("/student/profile")
    public ResponseEntity<Map<String, Object>> getProfile(@RequestParam String email) {
        User u = repository.findByEmail(email);
        if (u == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found."));
        }

        if (u.isMustChangePwd()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Change your temporary password first."));
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
}
