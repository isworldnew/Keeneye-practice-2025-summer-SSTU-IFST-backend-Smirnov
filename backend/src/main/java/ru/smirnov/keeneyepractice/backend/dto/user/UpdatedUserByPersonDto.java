package ru.smirnov.keeneyepractice.backend.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class UpdatedUserByPersonDto {

    private Long id;
    private Long userId;
    private String lastname;
    private String firstname;
    private String parentname;
    private LocalDate birthDate;
    private String phoneNumber;
    private String email;
    private String newUsername;
    private String role;
    private Boolean enabled;

}
