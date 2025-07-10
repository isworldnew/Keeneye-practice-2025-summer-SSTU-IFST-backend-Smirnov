package ru.smirnov.keeneyepractice.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.smirnov.keeneyepractice.backend.dto.basic.IncomingStudentByGroupDto;
import ru.smirnov.keeneyepractice.backend.dto.basic.OutcomingStudentByGroupDto;
import ru.smirnov.keeneyepractice.backend.mapper.StudentByGroupMapper;
import ru.smirnov.keeneyepractice.backend.repository.StudentByGroupRepository;

import java.util.List;

@Service
public class StudentByGroupService {

    private final StudentByGroupRepository studentByGroupRepository;
    private final StudentByGroupMapper studentByGroupMapper;

    @Autowired
    public StudentByGroupService(StudentByGroupRepository studentByGroupRepository, StudentByGroupMapper studentByGroupMapper) {
        this.studentByGroupRepository = studentByGroupRepository;
        this.studentByGroupMapper = studentByGroupMapper;
    }

    public ResponseEntity<Long> createStudentByGroup(IncomingStudentByGroupDto dto) {
        /* в силу того, что это просто boilerplate-код, пока не реализовал:
        данный метод в принципе не предполагает разграничение доступа по ролям*/
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    public ResponseEntity<List<OutcomingStudentByGroupDto>> findStudentsByGroups() {
        /* в силу того, что это просто boilerplate-код, пока не реализовал:
        данный метод в принципе не предполагает разграничение доступа по ролям*/
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    public ResponseEntity<OutcomingStudentByGroupDto> getStudentByGroupById(Long id) {
        /* в силу того, что это просто boilerplate-код, пока не реализовал:
        данный метод в принципе не предполагает разграничение доступа по ролям*/
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    public ResponseEntity<OutcomingStudentByGroupDto> updateStudentByGroupById(Long id, IncomingStudentByGroupDto dto) {
        /* в силу того, что это просто boilerplate-код, пока не реализовал:
        данный метод в принципе не предполагает разграничение доступа по ролям*/
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

}
