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

    public ResponseEntity<Student> getStudentById(Long id) {
        Student student = this.studentRepository.findById(id).orElse(null);

        if (student == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok(student);
    }

    public ResponseEntity<Student> updateStudentById(Long id, IncomingStudentDto dto) {
        Student student = this.studentRepository.findById(id).orElse(null);

        if (student == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        this.studentMapper.updateStudent(student, dto);
        this.studentRepository.save(student);

        return ResponseEntity.status(HttpStatus.OK).body(student);
    }

    public ResponseEntity<Void> deleteStudentById(Long id) {
        Student student = this.studentRepository.findById(id).orElse(null);

        if (student == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        this.studentRepository.delete(student);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
