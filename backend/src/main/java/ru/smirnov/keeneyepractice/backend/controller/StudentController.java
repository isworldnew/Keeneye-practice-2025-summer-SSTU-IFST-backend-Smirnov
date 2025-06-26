package ru.smirnov.keeneyepractice.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Column;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @PostMapping("/create-student")
    @Operation(summary = "Эндпоинт для создания записи о студенте. Перечислены только явно описанные возвращаемые HTTP-коды.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "Запись о студенте успешно создана.",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(
                                    type = "integer",
                                    format = "int64",
                                    example = "123"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Одно или более полей сохраняемой сущности имеют недопустимое значение (в том числе: случай отсутствия необходимого поля).",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"phoneNumber\": \"Phone number must be exactly 11 characters\", \"lastname\": \"must not be empty\"}")
                    )
            ),
    })
    public ResponseEntity<Long> createStudent(@RequestBody @Valid IncomingStudentDto dto) {
        return this.studentService.createStudent(dto);
    }

    @GetMapping("/students")
    @Operation(summary = "Эндпоинт для получения всех записей о студентах")
    @ApiResponse(
            responseCode = "200", description = "Получен список записей о студентах. Список может быть пустым.",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Student.class))
            )
    )
    public ResponseEntity<List<Student>> getStudents() {
        return this.studentService.getStudents();
    }

    @GetMapping("/student-by-id/{id}")
    @Operation(summary = "Эндпоинт для получения записи о студенте по его id. Перечислены только явно описанные возвращаемые HTTP-коды.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Запрос успешный, по данному id найдена запись о студенте.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Student.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Некорректное значение переменной запроса {id}.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"timestamp\": \"2025-06-26T11:53:54.648+00:00\", \"status\": 400, \"error\": \"Bad Request\", \"path\": \"/students-api/student-by-id/a\" }"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Для данного id не была найдена запись о студенте.",
                    content = @Content //пустое тело ответа
            )
    })
    public ResponseEntity<Student> getStudentById(@NotNull @Positive @PathVariable("id") Long id) {
        return this.studentService.getStudentById(id);
    }

    @PatchMapping("/update-student-by-id/{id}")
    @Operation(summary = "Эндпоинт для обновления записи студента по его id. Перечислены только явно описанные возвращаемые HTTP-коды.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Запись о студенте успешно обновлена.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Student.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Одно или более полей сохраняемой сущности имеют недопустимое значение (в том числе: случай отсутствия необходимого поля).\nНекорректное значение переменной запроса {id}.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"timestamp\": \"2025-06-26T11:53:54.648+00:00\", \"status\": 400, \"error\": \"Bad Request\", \"path\": \"/students-api/update-student-by-id/a\" }"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Для данного id не была найдена запись о студенте.",
                    content = @Content
            )

    })
    public ResponseEntity<Student> updateStudentById(
            @NotNull @Positive @PathVariable("id") Long id,
            @RequestBody @Valid IncomingStudentDto dto) {
        return this.studentService.updateStudentById(id, dto);
    }

    @DeleteMapping("/delete-student-by-id/{id}")
    @Operation(summary = "Эндпоинт для удаления записи о студенте по её id. Перечислены только явно описанные возвращаемые HTTP-коды.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Удаление успешное, тело ответа - пустое."),
            @ApiResponse(
                    responseCode = "400", description = "Некорректное значение переменной запроса {id}.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"timestamp\": \"2025-06-26T11:53:54.648+00:00\", \"status\": 400, \"error\": \"Bad Request\", \"path\": \"/students-api/delete-student-by-id/a\" }"
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Для данного id не была найдена запись о студенте.")
    })
    public ResponseEntity<Void> deleteStudentById(@NotNull @Positive @PathVariable("id") Long id) {
        return this.studentService.deleteStudentById(id);
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
