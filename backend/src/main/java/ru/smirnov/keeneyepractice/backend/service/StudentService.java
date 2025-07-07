package ru.smirnov.keeneyepractice.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.smirnov.keeneyepractice.backend.dto.IncomingStudentDto;
import ru.smirnov.keeneyepractice.backend.entity.*;
import ru.smirnov.keeneyepractice.backend.mapper.StudentMapper;
import ru.smirnov.keeneyepractice.backend.repository.StudentRepository;
import ru.smirnov.keeneyepractice.backend.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final UserRepository userRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository,
                          StudentMapper studentMapper,
                          UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.userRepository = userRepository;
    }

    public ResponseEntity<Long> createStudent(IncomingStudentDto dto) {
        Student student = studentMapper.incomingStudentDtoToStudentEntity(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                studentRepository.save(student).getId()
        );
    }

    public ResponseEntity<List<Student>> getStudents() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserEntity user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Student> students;
        switch (user.getRole()) {
            case ADMIN:
                students = studentRepository.findAll();
                break;
            case TEACHER:
                students = studentRepository.findByGroupTeacherId(user.getId());
                break;
            case STUDENT:
                students = studentRepository.findByGroupId(user.getStudent().getGroup().getId());
                break;
            default:
                throw new IllegalStateException("Unexpected role: " + user.getRole());
        }

        return ResponseEntity.ok(students);
    }

    public ResponseEntity<Student> getStudentById(Long id) {
        return (ResponseEntity<Student>) studentRepository.findById(id)
                .map(student -> {
                    if (hasAccessToStudent(student)) {
                        return ResponseEntity.ok(student);
                    }
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Student> updateStudentById(Long id, IncomingStudentDto dto) {
        return (ResponseEntity<Student>) studentRepository.findById(id)
                .map(student -> {
                    if (!hasAccessToStudent(student)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).<Student>build();
                    }

                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    UserEntity user = userRepository.findByUsername(userDetails.getUsername())
                            .orElseThrow(() -> new RuntimeException("User not found"));

                    if (user.getRole() == Role.STUDENT && !student.getUser().getId().equals(user.getId())) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }

                    studentMapper.updateStudent(student, dto);
                    return ResponseEntity.ok(studentRepository.save(student));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Void> deleteStudentById(Long id) {
        return (ResponseEntity<Void>) studentRepository.findById(id)
                .map(student -> {
                    if (!hasAccessToStudent(student)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).<Void>build();
                    }
                    studentRepository.delete(student);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private boolean hasAccessToStudent(Student student) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserEntity user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        switch (user.getRole()) {
            case ADMIN:
                return true;
            case TEACHER:
                return student.getGroup().getTeacher().getId().equals(user.getId());
            case STUDENT:
                return student.getUser().getId().equals(user.getId());
            default:
                return false;
        }
    }
}