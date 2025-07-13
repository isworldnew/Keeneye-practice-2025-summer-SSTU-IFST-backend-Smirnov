package ru.smirnov.keeneyepractice.backend.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.keeneyepractice.backend.dto.user.CreatedUserDataDto;
import ru.smirnov.keeneyepractice.backend.dto.user.UserToCreateDto;
import ru.smirnov.keeneyepractice.backend.projection.UserByPersonProjection;
import ru.smirnov.keeneyepractice.backend.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user-api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // создать user-а с записью в бизнесовой таблице
    @PostMapping("/register-user-with-business-data")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CreatedUserDataDto> createUserWithBusinessData(@Valid @RequestBody UserToCreateDto dto) {
        return this.userService.createUserWithBusinessData(dto);
    }

    // получить всех user-ов с их записями в бизнесовых таблицах
    @GetMapping("/find-all-users-and-entities-by-role/{role}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<UserByPersonProjection>> findAllUsersAndEntitiesByRole(
            @NotNull @NotBlank @NotEmpty @PathVariable String role
    ) {
        return this.userService.findAllUsersAndEntitiesByRole(role);
    }

    @GetMapping("/find-user-and-entity-by-role-and-entity-id/{role}/{entityId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserByPersonProjection> findUserAndEntityByRoleAndEntityId(
            @NotNull @NotBlank @NotEmpty @PathVariable String role,
            @NotNull @Positive @PathVariable Long entityId
    ) {
        return this.userService.findUserAndEntityByRoleAndEntityId(role, entityId);
    }

    // получить user-а и запись в бизнесовой таблице

    // обновить по id user-а
    // обновить по id сущности, но с обязательным указанием роли

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
