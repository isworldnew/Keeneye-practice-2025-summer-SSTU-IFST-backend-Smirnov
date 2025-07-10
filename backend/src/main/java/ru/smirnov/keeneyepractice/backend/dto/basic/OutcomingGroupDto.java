package ru.smirnov.keeneyepractice.backend.dto.basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class OutcomingGroupDto {

    private Long id;

    private String name;

    private Long teacherId;

}


