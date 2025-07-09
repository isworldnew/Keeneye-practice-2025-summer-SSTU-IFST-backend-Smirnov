package ru.smirnov.keeneyepractice.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.keeneyepractice.backend.mapper.TeacherMapper;
import ru.smirnov.keeneyepractice.backend.repository.TeacherRepository;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    private final TeacherMapper teacherMapper;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository, TeacherMapper teacherMapper) {
        this.teacherRepository = teacherRepository;
        this.teacherMapper = teacherMapper;
    }
}
