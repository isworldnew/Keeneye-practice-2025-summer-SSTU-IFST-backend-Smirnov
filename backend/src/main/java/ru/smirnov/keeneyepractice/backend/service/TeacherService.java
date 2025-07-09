package ru.smirnov.keeneyepractice.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.keeneyepractice.backend.mapper.TeacherMapper;
import ru.smirnov.keeneyepractice.backend.repository.TeacherRepository;

import java.util.Optional;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    private final TeacherMapper teacherMapper;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository, TeacherMapper teacherMapper) {
        this.teacherRepository = teacherRepository;
        this.teacherMapper = teacherMapper;
    }

    public Optional<Long> findTeacherIdByUserId(Long userId) {
        return this.teacherRepository.findTeacherIdByUserId(userId);
    }

}
