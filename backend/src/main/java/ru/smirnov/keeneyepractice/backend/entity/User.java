package ru.smirnov.keeneyepractice.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.smirnov.keeneyepractice.backend.entity.auxiliary.Role;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encodeRawPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

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


//    public User(String password) {
//        this.password = passwordEncoder.encode(password);
//    }

}
