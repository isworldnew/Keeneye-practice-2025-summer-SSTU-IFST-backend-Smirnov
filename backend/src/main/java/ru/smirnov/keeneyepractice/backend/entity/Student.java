package ru.smirnov.keeneyepractice.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "students")
@Data @AllArgsConstructor @NoArgsConstructor
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

    @Column(name = "study_group", columnDefinition = "VARCHAR(255)", nullable = false)
    @Schema(description = "Учебная группа студента", example = "Группа-101", minLength = 1, requiredMode = Schema.RequiredMode.REQUIRED)
    private String group;

    /*public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getParentname() {
        return parentname;
    }

    public void setParentname(String parentname) {
        this.parentname = parentname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }*/
}
//package ru.smirnov.keeneyepractice.backend.entity;
//
//import jakarta.persistence.*;
//
//import java.time.LocalDate;
//
//@Entity
//@Table(name = "students")
//public class Student {
//
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(columnDefinition = "TEXT", nullable = false)
//    private String lastname;
//
//    @Column(columnDefinition = "TEXT", nullable = false)
//    private String firstname;
//
//    @Column(columnDefinition = "TEXT")
//    private String parentname;
//
//    @Column(columnDefinition = "DATE", name = "birth_date", nullable = false)
//    private LocalDate birthDate;
//
//    @Column(columnDefinition = "CHAR(11)", name = "phone_number", nullable = false)
//    private String phoneNumber;
//
//    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
//    private String email;
//
//    @Column(name = "study_group", columnDefinition = "VARCHAR(255)", nullable = false)
//    private String group;
//
//
//    // Решил пока не баловаться с Lombok, просто сгенерировал через IDE
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getLastname() {
//        return lastname;
//    }
//
//    public void setLastname(String lastname) {
//        this.lastname = lastname;
//    }
//
//    public String getFirstname() {
//        return firstname;
//    }
//
//    public void setFirstname(String firstname) {
//        this.firstname = firstname;
//    }
//
//    public String getParentname() {
//        return parentname;
//    }
//
//    public void setParentname(String parentname) {
//        this.parentname = parentname;
//    }
//
//    public LocalDate getBirthDate() {
//        return birthDate;
//    }
//
//    public void setBirthDate(LocalDate birthDate) {
//        this.birthDate = birthDate;
//    }
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getGroup() {
//        return group;
//    }
//
//    public void setGroup(String group) {
//        this.group = group;
//    }
//}
