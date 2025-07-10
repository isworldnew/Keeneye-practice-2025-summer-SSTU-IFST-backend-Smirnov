package ru.smirnov.keeneyepractice.backend.dto.basic;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class IncomingStudentByGroupDto {

    @NotNull @Positive
    private Long studentId;

    @NotNull @Positive
    private Long groupId;

}
