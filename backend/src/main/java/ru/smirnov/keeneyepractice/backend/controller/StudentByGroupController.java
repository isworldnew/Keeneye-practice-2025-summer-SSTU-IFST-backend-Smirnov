package ru.smirnov.keeneyepractice.backend.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.keeneyepractice.backend.dto.basic.IncomingStudentByGroupDto;
import ru.smirnov.keeneyepractice.backend.dto.basic.OutcomingGroupDto;
import ru.smirnov.keeneyepractice.backend.dto.basic.OutcomingStudentByGroupDto;
import ru.smirnov.keeneyepractice.backend.service.StudentByGroupService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student-by-group-api")
public class StudentByGroupController {

    private final StudentByGroupService studentByGroupService;

    @Autowired
    public StudentByGroupController(StudentByGroupService studentByGroupService) {
        this.studentByGroupService = studentByGroupService;
    }

    @PostMapping("/create-student-by-group")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Long> createStudentByGroup(@Valid @RequestBody IncomingStudentByGroupDto dto) {
        return this.studentByGroupService.createStudentByGroup(dto);
    }

    @PostMapping("/find-students-by-groups")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<OutcomingStudentByGroupDto>> findStudentsByGroups() {
        return this.studentByGroupService.findStudentsByGroups();
    }

    @GetMapping("/student-by-group-by-id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<OutcomingStudentByGroupDto> getStudentByGroupById(@NotNull @Positive @PathVariable Long id) {
        return this.studentByGroupService.getStudentByGroupById(id);
    }

    @PatchMapping("/update-student-by-group-by-id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<OutcomingStudentByGroupDto> updateStudentByGroupById(
            @NotNull @Positive @PathVariable Long id,
            @Valid @RequestBody IncomingStudentByGroupDto dto
    ) {
        return this.studentByGroupService.updateStudentByGroupById(id, dto);
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
