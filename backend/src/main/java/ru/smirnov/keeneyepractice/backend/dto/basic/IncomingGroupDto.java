package ru.smirnov.keeneyepractice.backend.dto.basic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class IncomingGroupDto {

    @NotNull @NotBlank @NotEmpty
    private String name;

    @NotNull @Positive
    private Long teacherId;

}
