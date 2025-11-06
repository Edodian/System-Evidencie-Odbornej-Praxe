// src/main/java/sk/ukf/sep/entity/User.java
package sk.ukf.sep.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter @Setter
@ToString(exclude = "pwd")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Integer id;

    @Column(unique = true, nullable = false)
    private String email;

    @com.fasterxml.jackson.annotation.JsonProperty(access = com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String pwd;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false)
    private String role;

    @Column(name = "alt_email")
    private String altEmail;
    @Column(name = "phone")
    private String phone;
    @Column
    private String field;

    @Column(name = "must_change_pwd", nullable = false)
    private boolean mustChangePwd = true;

    @org.hibernate.annotations.CreationTimestamp
    private java.time.LocalDateTime createdAt;
    @org.hibernate.annotations.UpdateTimestamp
    private java.time.LocalDateTime updatedAt;

    @PrePersist
    private void ensurePassword() {
        if (this.pwd == null || this.pwd.isBlank()) {
            this.pwd = sk.ukf.sep.util.PasswordUtil.generate(14); // no hashing for now
        }
        this.mustChangePwd = true; // new account pwd change
    }
}
