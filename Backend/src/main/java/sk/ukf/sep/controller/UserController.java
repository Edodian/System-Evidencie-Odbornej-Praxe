package sk.ukf.sep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sk.ukf.sep.repository.UserRepository;
import sk.ukf.sep.entity.User;

@RestController
public class UserController {

    final private UserRepository repository;

    @Autowired
    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/student")
    public String registerStudent(
            @RequestBody User student
    ) {
        System.out.println(student);
        if (student.getEmail().endsWith("@student.ukf.sk")){
            System.out.println(student);
            return repository.save(student).toString();
        }
        else return "Email provided is not UKF student mail";
    }

    @PostMapping("/login")
    public String loginStudent(
            @RequestBody User credentials
    ) {
        User user = repository.findByEmail(credentials.getEmail());
        if (user != null && user.getPwd().equals(credentials.getPwd())) {
            return "Login successful: " + user.toString();
        }
        return "Invalid credentials";
    }

}
