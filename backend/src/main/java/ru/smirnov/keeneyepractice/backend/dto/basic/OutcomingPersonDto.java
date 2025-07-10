package ru.smirnov.keeneyepractice.backend.dto.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class OutcomingPersonDto extends IncomingPersonDto {

    private Long id;

    private String lastname;

    private String firstname;

    private String parentname;

    private LocalDate birthDate;

    private String phoneNumber;

    private String email;

}
