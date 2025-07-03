package ru.smirnov.keeneyepractice.backend.dto.auth;

import lombok.Data;

@Data
public class JwtResponse {
    private final String token;
}