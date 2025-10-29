package sk.ukf.sep;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter @ToString
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Integer id;
    @Column(unique = true)
    private String email;
    @Column(nullable = false)
    private String pwd;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false)
//    private String role;
//    @Column(name="titul_pref")
//    private String titulPref;
//    @Column(name="titul_suf")
//    private String titulSuf;
//    @Column(name="alt_email")
//    private String altEmail;
//    @Column
//    private String field; // TODO: Change to Field class when will ready
//    @Column
//    private String address; // TODO: Change to Address class when will ready
//    @Column
//    private String organization; // TODO: Change to Organization class when will ready
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
