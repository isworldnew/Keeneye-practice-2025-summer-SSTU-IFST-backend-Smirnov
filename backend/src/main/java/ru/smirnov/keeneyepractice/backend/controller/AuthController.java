package ru.smirnov.keeneyepractice.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.smirnov.keeneyepractice.backend.dto.auth.JwtRequest;
import ru.smirnov.keeneyepractice.backend.dto.auth.JwtResponse;
import ru.smirnov.keeneyepractice.backend.service.auth.JwtUserDetailsService;
import ru.smirnov.keeneyepractice.backend.service.auth.TokenManager;

@Tag(name = "authentication_endpoints")
@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailsService userDetailsService;
    private final TokenManager tokenManager;

    @Operation(summary = "Authenticate user and get JWT token")
    @ApiResponse(responseCode = "200", description = "Successfully authenticated")
    @ApiResponse(responseCode = "401", description = "Bad credentials")
    @PostMapping("/api/auth/login")
    public JwtResponse createToken(@RequestBody JwtRequest request) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        return new JwtResponse(tokenManager.generateJwtToken(userDetails));
    }
}