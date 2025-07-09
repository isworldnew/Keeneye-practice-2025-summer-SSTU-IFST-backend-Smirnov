package ru.smirnov.keeneyepractice.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@MappedSuperclass
@Data
public abstract class Person {

    // пока что поля у Student и Teacher полностью совпадают,
    // но в реальном кейсе они запросто могут сильно различаться

    // пока они полностью совпадают - решил вынести всё это в отдельный класс

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String lastname;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String firstname;

    @Column(columnDefinition = "TEXT")
    private String parentname;

    @Column(columnDefinition = "DATE", name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(columnDefinition = "CHAR(11)", name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String email;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

}
