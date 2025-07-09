package ru.smirnov.keeneyepractice.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.smirnov.keeneyepractice.backend.entity.auxiliary.Role;

@Entity
@Table(name = "users")
@Data
public class User {

    static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String username;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column
    private boolean enabled;

    @OneToOne(mappedBy = "user")
    @JsonManagedReference
    private Student student;

    @OneToOne(mappedBy = "user")
    @JsonManagedReference
    private Teacher teacher;


    public User(String password) {
        this.password = passwordEncoder.encode(password);
    }

}
