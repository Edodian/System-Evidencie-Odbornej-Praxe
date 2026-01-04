// src/main/java/sk/ukf/sep/entity/User.java
package sk.ukf.sep.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sk.ukf.sep.util.PasswordUtil;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter @Setter
@ToString(exclude = "pwd")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(unique = true, nullable = false, length = 64)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String pwd;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String tempPwd;

    @Column(nullable = false, length = 32)
    private String name;

    @Column(nullable = false, length = 32)
    private String surname;

    @Column(nullable = false, length = 16)
    private String role;

    @Column(name = "alt_email", length = 64)
    private String altEmail;

    @Column(length = 20)
    private String phone;

    @Column(length = 32)
    private String field;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    private void ensurePassword() {
        if ((this.pwd == null || this.pwd.isBlank()) && (this.tempPwd == null || this.tempPwd.isBlank())) {
            this.tempPwd = PasswordUtil.generate(8);
        }
    }
}

