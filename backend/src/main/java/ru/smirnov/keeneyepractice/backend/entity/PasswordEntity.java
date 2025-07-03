package ru.smirnov.keeneyepractice.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "passwords")
@Getter @Setter
@NoArgsConstructor
public class PasswordEntity {

    static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public PasswordEntity(String password) {
        this.password = passwordEncoder.encode(password);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    @JsonIgnore
    private void setPasswordWithEncoding(String password) {
        this.password = passwordEncoder.encode(password);
    }
}