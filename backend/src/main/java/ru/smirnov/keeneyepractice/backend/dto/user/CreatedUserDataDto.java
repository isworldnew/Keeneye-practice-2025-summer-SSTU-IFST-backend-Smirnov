package ru.smirnov.keeneyepractice.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class CreatedUserDataDto {

    private Long entityId;

    private Long userId;

}
