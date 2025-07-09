package ru.smirnov.keeneyepractice.backend.dto.authorization;

import lombok.Data;

@Data
public class JwtResponseDto {

    private final String jwtToken;

}
