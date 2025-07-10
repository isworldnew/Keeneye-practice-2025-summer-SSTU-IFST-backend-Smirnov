package ru.smirnov.keeneyepractice.backend.dto.authorization;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @AllArgsConstructor @NoArgsConstructor
public class IncomingPersonDto {

    @NotNull @NotBlank @NotEmpty
    private String lastname;

    @NotNull @NotBlank @NotEmpty
    private String firstname;

    private String parentname;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotNull @NotBlank @NotEmpty
    @Size(min = 11, max = 11, message = "Phone number must be exactly 11 characters")
    private String phoneNumber;

    @NotNull @NotBlank @NotEmpty @Email
    private String email;

}


