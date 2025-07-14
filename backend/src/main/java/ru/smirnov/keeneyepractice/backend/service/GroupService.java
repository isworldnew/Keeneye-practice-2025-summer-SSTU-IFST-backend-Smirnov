package ru.smirnov.keeneyepractice.backend.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.smirnov.keeneyepractice.backend.dto.basic.IncomingGroupDto;
import ru.smirnov.keeneyepractice.backend.dto.basic.OutcomingGroupDto;
import ru.smirnov.keeneyepractice.backend.entity.Group;
import ru.smirnov.keeneyepractice.backend.entity.Teacher;
import ru.smirnov.keeneyepractice.backend.mapper.GroupMapper;
import ru.smirnov.keeneyepractice.backend.repository.GroupRepository;
import ru.smirnov.keeneyepractice.backend.repository.TeacherRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final TeacherRepository teacherRepository;

    private final GroupMapper groupMapper;

    @Autowired
    public GroupService(GroupRepository groupRepository, GroupMapper groupMapper, TeacherRepository teacherRepository) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
        this.teacherRepository = teacherRepository;
    }

    public ResponseEntity<Long> createGroup(IncomingGroupDto dto) {

        Teacher teacher = this.teacherRepository.findById(dto.getTeacherId()).orElse(null);

        if (teacher == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        Group group = new Group();
        group.setName(dto.getName());
        group.setTeacher(teacher);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.groupRepository.save(group).getId());

    }

    public ResponseEntity<List<OutcomingGroupDto>> getGroups() {
        List<Group> groups = this.groupRepository.findAll();

        List<OutcomingGroupDto> groupDtoList = groups.stream()
                .map(group -> new OutcomingGroupDto(
                        group.getId(),
                        group.getName(),
                        group.getTeacher().getId())
                )
                .collect(Collectors.toList());

        return ResponseEntity.ok(groupDtoList);
    }

    public ResponseEntity<OutcomingGroupDto> getGroupById(Long id) {

        Group group = this.groupRepository.findById(id).orElse(null);

        if (group == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok(
                new OutcomingGroupDto(
                        group.getId(),
                        group.getName(),
                        group.getTeacher().getId()
                )
        );

    }

    public ResponseEntity<OutcomingGroupDto> updateGroupById(Long id, IncomingGroupDto dto) {

        Group group = this.groupRepository.findById(dto.getTeacherId()).orElse(null);

        Teacher teacher = this.teacherRepository.findById(dto.getTeacherId()).orElse(null);

        if (group == null || teacher == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        group.setName(dto.getName());
        group.setTeacher(teacher);
        this.groupRepository.save(group);

        return ResponseEntity.ok(
                new OutcomingGroupDto(
                        group.getId(),
                        group.getName(),
                        group.getTeacher().getId()
                )
        );

    }

}
