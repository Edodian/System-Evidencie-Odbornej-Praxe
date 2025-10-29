package sk.ukf.sep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
