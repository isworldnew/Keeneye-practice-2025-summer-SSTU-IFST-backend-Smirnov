package ru.smirnov.keeneyepractice.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "students")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор студента", example = "1", requiredMode = Schema.RequiredMode.AUTO)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    @Schema(description = "Фамилия студента", example = "Иванов", minLength = 1, requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastname;

    @Column(columnDefinition = "TEXT", nullable = false)
    @Schema(description = "Имя студента", example = "Иван", minLength = 1, requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstname;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Отчество студента", example = "Иванович")
    private String parentname;

    @Column(columnDefinition = "DATE", name = "birth_date", nullable = false)
    @Schema(description = "Дата рождения студента", example = "2000-01-01", type = "string", format = "date", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate birthDate;

    @Column(columnDefinition = "CHAR(11)", name = "phone_number", nullable = false)
    @Schema(description = "Номер телефона студента", example = "89991234567", minLength = 11, maxLength = 11, requiredMode = Schema.RequiredMode.REQUIRED)
    private String phoneNumber;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    @Schema(description = "Email студента", example = "student@example.com", minLength = 1, requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}