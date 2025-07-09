package ru.smirnov.keeneyepractice.backend.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.keeneyepractice.backend.dto.authorization.JwtRequestDto;
import ru.smirnov.keeneyepractice.backend.dto.authorization.JwtResponseDto;
import ru.smirnov.keeneyepractice.backend.service.UserService;
import ru.smirnov.keeneyepractice.backend.service.authentication.TokenManager;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenManager tokenManager;

    @Autowired
    public AuthenticationController(
            UserService userService,
            AuthenticationManager authenticationManager,
            TokenManager tokenManager
    ) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> createToken(@RequestBody @Valid JwtRequestDto requestDto) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword())
            );
        }
        catch (DisabledException e) {
            throw new Exception("User is disabled", e);
        }
        catch (BadCredentialsException e) {
            throw new Exception("Invalid credentials (username or password)", e);
        }

        UserDetails userDetails = this.userService.loadUserByUsername(requestDto.getUsername());

        String token = this.tokenManager.generateJwtToken(userDetails);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new JwtResponseDto(token)
        );

    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().stream()
                .forEach(error -> {
                    String fieldName = ((FieldError)error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.badRequest().body("Validation failed: " + ex.getMessage());
    }

}
