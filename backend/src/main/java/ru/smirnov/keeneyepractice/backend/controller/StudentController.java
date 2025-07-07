package ru.smirnov.keeneyepractice.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import ru.smirnov.keeneyepractice.backend.dto.IncomingStudentDto;
import ru.smirnov.keeneyepractice.backend.entity.Student;
import ru.smirnov.keeneyepractice.backend.service.StudentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "students_endpoints")
@RestController
@RequestMapping("/students-api")
@SecurityRequirement(name = "bearerAuth")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/create-student")
    @Operation(summary = "Создать запись о студенте")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> createStudent(@RequestBody @Valid IncomingStudentDto dto) {
        return studentService.createStudent(dto);
    }

    @GetMapping("/students")
    @Operation(summary = "Получить всех студентов (доступ зависит от роли)")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    public ResponseEntity<List<Student>> getStudents() {
        return studentService.getStudents();
    }

    @GetMapping("/student-by-id/{id}")
    @Operation(summary = "Получить студента по ID (доступ зависит от роли)")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    public ResponseEntity<Student> getStudentById(@NotNull @Positive @PathVariable("id") Long id) {
        return studentService.getStudentById(id);
    }

    @PatchMapping("/update-student-by-id/{id}")
    @Operation(summary = "Обновить студента по ID (доступ зависит от роли)")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    public ResponseEntity<Student> updateStudentById(
            @NotNull @Positive @PathVariable("id") Long id,
            @RequestBody @Valid IncomingStudentDto dto) {
        return studentService.updateStudentById(id, dto);
    }

    @DeleteMapping("/delete-student-by-id/{id}")
    @Operation(summary = "Удалить студента по ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStudentById(@NotNull @Positive @PathVariable("id") Long id) {
        return studentService.deleteStudentById(id);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
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