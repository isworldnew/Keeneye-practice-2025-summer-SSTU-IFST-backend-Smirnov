package ru.smirnov.keeneyepractice.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.keeneyepractice.backend.mapper.StudentMapper;
import ru.smirnov.keeneyepractice.backend.repository.StudentRepository;

import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    @Autowired
    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    public Optional<Long> findStudentIdByUserId(Long userId) {
        return this.studentRepository.findStudentIdByUserId(userId);
    }
}
