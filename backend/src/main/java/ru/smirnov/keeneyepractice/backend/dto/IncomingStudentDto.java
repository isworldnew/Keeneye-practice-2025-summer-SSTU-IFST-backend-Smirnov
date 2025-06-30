package ru.smirnov.keeneyepractice.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @AllArgsConstructor @NoArgsConstructor
public class IncomingStudentDto {

    @Schema(description = "Фамилия студента", example = "Иванов", minLength = 1, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull @NotBlank @NotEmpty
    private String lastname;

    @Schema(description = "Имя студента", example = "Иван", minLength = 1, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull @NotBlank @NotEmpty
    private String firstname;

    @Schema(description = "Отчество студента", example = "Иванович")
    private String parentname;

    @Schema(description = "Дата рождения студента", example = "2000-01-01", type = "string", format = "date", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Schema(description = "Номер телефона студента", example = "89991234567", minLength = 11, maxLength = 11, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull @NotBlank @NotEmpty
    @Size(min = 11, max = 11, message = "Phone number must be exactly 11 characters")
    private String phoneNumber;

    @Schema(description = "Email студента", example = "student@example.com", minLength = 1, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull @NotBlank @NotEmpty @Email
    private String email;

    @Schema(description = "Учебная группа студента", example = "Группа-101", minLength = 1, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull @NotBlank @NotEmpty
    private String group;

    /*public String getLastname() {
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
    }*/
}