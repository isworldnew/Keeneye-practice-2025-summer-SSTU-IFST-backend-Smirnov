package ru.smirnov.keeneyepractice.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class IncomingStudentDto {

    @NotNull @NotBlank @NotEmpty
    private String lastname;

    @NotNull @NotBlank @NotEmpty
    private String firstname;

    private String parentname;

    @NotNull @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotNull @NotBlank @NotEmpty
    @Size(min = 11, max = 11, message = "Phone number must be exactly 11 characters")
    private String phoneNumber;

    @NotNull @NotBlank @NotEmpty @Email
    private String email;

    @NotNull @NotBlank @NotEmpty
    // тут бы дописать полноценные валидации для нужного формата строки
    private String group;


    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getParentname() {
        return parentname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getGroup() {
        return group;
    }
}
