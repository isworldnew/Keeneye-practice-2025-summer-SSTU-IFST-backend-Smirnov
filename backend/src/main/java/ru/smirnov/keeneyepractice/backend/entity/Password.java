//package ru.smirnov.keeneyepractice.backend.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@Entity
//@Table(name = "passwords")
//@Data @NoArgsConstructor @AllArgsConstructor
//public class Password {
//
//    static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//    @Id @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    private String password;
//
//    public Password(String password) {
//        this.password = passwordEncoder.encode(password);
//    }
//
//    @JsonIgnore
//    private void setPasswordWithEncoding(String password) {
//        this.password = passwordEncoder.encode(password);
//    }
//
//}
