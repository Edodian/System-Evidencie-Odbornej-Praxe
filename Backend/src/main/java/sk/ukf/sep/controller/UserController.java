package sk.ukf.sep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sk.ukf.sep.repository.UserRepository;
import sk.ukf.sep.entity.User;

@RestController
public class UserController {

    final private UserRepository repository;
    final private PasswordEncoder passwordEncoder;
    final private AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserRepository repository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/student")
    public ResponseEntity<User> registerStudent(
            @RequestBody User student
    ) {
        System.out.println(student);
        if (student.getEmail().endsWith("@student.ukf.sk")){
            // Hash the password before saving
            student.setPwd(passwordEncoder.encode(student.getPwd()));
            User savedStudent = repository.save(student);
            System.out.println(savedStudent);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginStudent(
            @RequestBody User credentials
    ) {
        try {
            // Create authentication token with credentials
            UsernamePasswordAuthenticationToken authenticationToken = 
                    new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPwd());
            
            // Authenticate using Spring Security's AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            
            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Retrieve the authenticated user from database
            User user = repository.findByEmail(credentials.getEmail());
            
            return ResponseEntity.ok(user);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
