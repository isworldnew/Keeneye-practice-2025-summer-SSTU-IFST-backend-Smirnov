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
import ru.smirnov.keeneyepractice.backend.dto.basic.IncomingPersonDto;
import ru.smirnov.keeneyepractice.backend.dto.basic.OutcomingPersonDto;
import ru.smirnov.keeneyepractice.backend.projection.PersonProjection;
import ru.smirnov.keeneyepractice.backend.service.StudentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student-api")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/student-by-id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER')")
    public ResponseEntity<PersonProjection> findStudentById(@NotNull @Positive @PathVariable Long id) {
        return this.studentService.findStudentById(id);
    }

    @GetMapping("/students")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER')")
    public ResponseEntity<List<PersonProjection>> findAllStudents() {
        return this.studentService.findAllStudents();
    }

    @PatchMapping("/update-student-by-id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER')")
    public ResponseEntity<OutcomingPersonDto> updateStudentById(
            @NotNull @Positive @PathVariable Long id,
            @Valid @RequestBody IncomingPersonDto dto
    ) {
        return this.studentService.updateStudentById(id, dto);
    }

//    есть в UserController
//    @PostMapping("/create-student")
//    @PreAuthorize("hasAnyRole('ADMIN')")
//    public ResponseEntity<Long> createTeacher(@Valid @RequestBody IncomingPersonDto dto) {
//        return this.studentService.createStudent(dto);
//    }

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
