// src/main/java/sk/ukf/sep/controller/UserController.java
package sk.ukf.sep.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import sk.ukf.sep.entity.User;
import sk.ukf.sep.repository.UserRepository;

import java.util.Map;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/users")
public class UserController {

    private final UserRepository repository;

    @PostMapping("/student")
    public ResponseEntity<Map<String, Object>> registerStudent(@RequestBody User student) {
        if (student == null || student.getEmail() == null || !student.getEmail().endsWith("@student.ukf.sk")) {
            return ResponseEntity.badRequest().build();
        }
        student.setPwd(null);                // trigger @PrePersist to auto-generate
        User saved = repository.save(student);

        // Previously returned UserRegistrationDTO(id, email, tempPassword)
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "id", saved.getId(),
                        "email", saved.getEmail(),
                        "pwd", saved.getPwd()   // temp only (same as before)
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        String password = req.get("password");

        User u = repository.findByEmail(email);
        if (u == null || u.getPwd() == null || !u.getPwd().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (u.isMustChangePwd()) { // if first login
            // Previously returned new AuthResponse("PASSWORD_CHANGE_REQUIRED", u.getId())
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "status", "PASSWORD_CHANGE_REQUIRED",
                            "userId", u.getId()
                    ));
        }
        // Previously returned new AuthResponse("OK", u.getId())
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        u.setPwd(newPassword);   // no hashing for now (same as before)
        u.setMustChangePwd(false);
        repository.save(u);

        // Previously returned new AuthResponse("PASSWORD_CHANGED", u.getId())
        return ResponseEntity.ok(Map.of(
                "status", "PASSWORD_CHANGED",
                "userId", u.getId()
        ));
    }
}
