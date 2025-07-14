package ru.smirnov.keeneyepractice.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.smirnov.keeneyepractice.backend.dto.basic.IncomingStudentByGroupDto;
import ru.smirnov.keeneyepractice.backend.dto.basic.OutcomingStudentByGroupDto;
import ru.smirnov.keeneyepractice.backend.entity.Group;
import ru.smirnov.keeneyepractice.backend.entity.Student;
import ru.smirnov.keeneyepractice.backend.entity.StudentByGroup;
import ru.smirnov.keeneyepractice.backend.mapper.StudentByGroupMapper;
import ru.smirnov.keeneyepractice.backend.repository.GroupRepository;
import ru.smirnov.keeneyepractice.backend.repository.StudentByGroupRepository;
import ru.smirnov.keeneyepractice.backend.repository.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentByGroupService {

    private final StudentByGroupRepository studentByGroupRepository;
    private final StudentByGroupMapper studentByGroupMapper;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public StudentByGroupService(
            StudentByGroupRepository studentByGroupRepository,
            StudentByGroupMapper studentByGroupMapper,
            StudentRepository studentRepository,
            GroupRepository groupRepository
    ) {
        this.studentByGroupRepository = studentByGroupRepository;
        this.studentByGroupMapper = studentByGroupMapper;
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    public ResponseEntity<Long> createStudentByGroup(IncomingStudentByGroupDto dto) {
        Student student = this.studentRepository.findById(dto.getStudentId()).orElse(null);
        Group group = this.groupRepository.findById(dto.getGroupId()).orElse(null);

        if (student == null || group == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        StudentByGroup studentByGroup = new StudentByGroup();
        studentByGroup.setStudent(student);
        studentByGroup.setGroup(group);

        Long id = this.studentByGroupRepository.save(studentByGroup).getId();

        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    public ResponseEntity<List<OutcomingStudentByGroupDto>> findStudentsByGroups() {

        List<StudentByGroup> studentsByGroups = this.studentByGroupRepository.findAll();

        List<OutcomingStudentByGroupDto> dtos = studentsByGroups.stream()
                .map(
                        studentByGroup -> new OutcomingStudentByGroupDto(
                                studentByGroup.getId(),
                                studentByGroup.getStudent().getId(),
                                studentByGroup.getStudent().getId()
                        )
                )
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);

    }

    public ResponseEntity<OutcomingStudentByGroupDto> getStudentByGroupById(Long id) {

        StudentByGroup studentByGroup = this.studentByGroupRepository.findById(id).orElse(null);

        if (studentByGroup == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok(
                new OutcomingStudentByGroupDto(
                        studentByGroup.getId(),
                        studentByGroup.getStudent().getId(),
                        studentByGroup.getStudent().getId()
                )
        );

    }

    public ResponseEntity<OutcomingStudentByGroupDto> updateStudentByGroupById(Long id, IncomingStudentByGroupDto dto) {
        Student student = this.studentRepository.findById(dto.getStudentId()).orElse(null);
        Group group = this.groupRepository.findById(dto.getGroupId()).orElse(null);
        StudentByGroup studentByGroup = this.studentByGroupRepository.findById(id).orElse(null);

        if (student == null || group == null || studentByGroup == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        studentByGroup.setStudent(student);
        studentByGroup.setGroup(group);

        this.studentByGroupRepository.save(studentByGroup);

        return ResponseEntity.ok(
                new OutcomingStudentByGroupDto(
                        studentByGroup.getId(),
                        studentByGroup.getStudent().getId(),
                        studentByGroup.getStudent().getId()
                )
        );

    }

}
