package ru.smirnov.keeneyepractice.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.keeneyepractice.backend.dto.GroupDto;
import ru.smirnov.keeneyepractice.backend.entity.Group;
import ru.smirnov.keeneyepractice.backend.service.GroupService;

import java.util.List;

@Tag(name = "groups_endpoints")
@RestController
@RequestMapping("/groups-api")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/create-group")
    @Operation(summary = "Создать новую группу")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> createGroup(@RequestBody GroupDto dto) {
        return groupService.createGroup(dto);
    }

    @GetMapping("/groups")
    @Operation(summary = "Получить все группы")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<Group>> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/group-by-id/{id}")
    @Operation(summary = "Получить группу по ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        return groupService.getGroupById(id);
    }

    @PutMapping("/update-group/{id}")
    @Operation(summary = "Обновить группу")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Group> updateGroup(@PathVariable Long id, @RequestBody GroupDto dto) {
        return groupService.updateGroup(id, dto);
    }

    @DeleteMapping("/delete-group/{id}")
    @Operation(summary = "Удалить группу")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        return groupService.deleteGroup(id);
    }

    @GetMapping("/groups-by-teacher/{teacherId}")
    @Operation(summary = "Получить группы по преподавателю")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<Group>> getGroupsByTeacher(@PathVariable Long teacherId) {
        return groupService.getGroupsByTeacher(teacherId);
    }
}