package ru.smirnov.keeneyepractice.backend.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class UserByPersonForUpdateDto {

    @NotNull @NotBlank @NotEmpty
    private String lastname;

    @NotNull @NotBlank @NotEmpty
    private String firstname;

    @NotNull @NotBlank @NotEmpty
    private String parentname;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotNull @NotBlank @NotEmpty
    @Size(min = 11, max = 11, message = "Phone number must be exactly 11 characters")
    private String phoneNumber;

    @NotNull @NotBlank @NotEmpty @Email
    private String email;

    @NotNull @NotBlank @NotEmpty
    private String oldUsername;

    @NotNull @NotBlank @NotEmpty
    private String newUsername;

    @NotNull @NotBlank @NotEmpty
    @Size(min = 10, message = "Password's size should be >= 10")
    private String rawOldPassword;

    @NotNull @NotBlank @NotEmpty
    @Size(min = 10, message = "Password's size should be >= 10")
    private String rawNewPassword;

    @NotNull
    private Boolean enabled;

}
