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
import ru.smirnov.keeneyepractice.backend.dto.basic.IncomingGroupDto;
import ru.smirnov.keeneyepractice.backend.dto.basic.OutcomingGroupDto;
import ru.smirnov.keeneyepractice.backend.service.GroupService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/group-api")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping("/create-group")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Long> createGroup(@Valid @RequestBody IncomingGroupDto dto) {
        return this.groupService.createGroup(dto);
    }

    @GetMapping("/groups")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<OutcomingGroupDto>> getGroups() {
        return this.groupService.getGroups();
    }

    @GetMapping("/group-by-id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<OutcomingGroupDto> getGroupById(@NotNull @Positive @PathVariable Long id) {
        return this.groupService.getGroupById(id);
    }

    @PatchMapping("/update-group-by-id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<OutcomingGroupDto> updateGroupById(
            @NotNull @Positive @Valid Long id,
            @Valid @RequestBody IncomingGroupDto dto
    ) {
        return this.groupService.updateGroupById(id, dto);
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
