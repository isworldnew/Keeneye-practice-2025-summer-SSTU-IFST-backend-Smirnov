package ru.smirnov.keeneyepractice.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.smirnov.keeneyepractice.backend.dto.IncomingStudentDto;
import ru.smirnov.keeneyepractice.backend.entity.Student;
import ru.smirnov.keeneyepractice.backend.mapper.StudentMapper;
import ru.smirnov.keeneyepractice.backend.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Autowired
    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }


    public ResponseEntity<Long> createStudent(IncomingStudentDto dto) {
        Student student = this.studentMapper.incomingStudentDtoToStudentEntity(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.studentRepository.save(student).getId()
        );
    }

    public ResponseEntity<List<Student>> getStudents() {
        List<Student> students = this.studentRepository.findAll();
        return ResponseEntity.ok(students);
    }

}
