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
    public User registerStudent(
            @RequestBody User student
    ) {
        String mail = student.getEmail();
        if (mail.substring(mail.length() - 7).equals("@studentmail.ukf.sk")){
            System.out.println(student);
            return repository.save(student);
        }
        else return null;
    }

}
