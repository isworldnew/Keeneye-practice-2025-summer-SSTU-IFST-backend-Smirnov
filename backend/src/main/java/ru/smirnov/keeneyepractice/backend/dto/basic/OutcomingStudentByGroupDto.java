package ru.smirnov.keeneyepractice.backend.dto.basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class OutcomingStudentByGroupDto {

    private Long id;

    private Long studentId;

    private Long groupId;

}
