package ru.smirnov.keeneyepractice.backend.dto.authentication;

import lombok.Data;

@Data
public class JwtResponseDto {

    private final String jwtToken;

}
