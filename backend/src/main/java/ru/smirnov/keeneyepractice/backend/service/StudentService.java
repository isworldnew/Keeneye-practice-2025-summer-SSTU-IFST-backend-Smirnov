package ru.smirnov.keeneyepractice.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.smirnov.keeneyepractice.backend.dto.authorization.DataForToken;
import ru.smirnov.keeneyepractice.backend.mapper.StudentMapper;
import ru.smirnov.keeneyepractice.backend.projection.PersonProjection;
import ru.smirnov.keeneyepractice.backend.projection.StudentInTeachersGroupProjection;
import ru.smirnov.keeneyepractice.backend.repository.GroupRepository;
import ru.smirnov.keeneyepractice.backend.repository.StudentRepository;

import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    private final StudentMapper studentMapper;

    @Autowired
    public StudentService(
            StudentRepository studentRepository,
            GroupRepository groupRepository,
            StudentMapper studentMapper
    ) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
        this.studentMapper = studentMapper;
    }

    public Optional<Long> findStudentIdByUserId(Long userId) {
        return this.studentRepository.findStudentIdByUserId(userId);
    }

    public ResponseEntity<PersonProjection> findStudentById(Long studentId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        DataForToken tokenData = null;

        if (authentication != null && authentication.getPrincipal() instanceof DataForToken)
            tokenData = (DataForToken) authentication.getPrincipal();

        // вместо того, чтобы в трёх местах в коде писать извлечение студента,
        // извлеку его сразу, а потом определю, возвращать его или нет...
        // вероятно, решение - сомнительное

        PersonProjection student = this.studentRepository.findStudentById(studentId).orElse(null);

        // "может просматривать информацию о себе"
        if (tokenData.getRole().equals("ROLE_STUDENT") && !tokenData.getEntityId().equals(studentId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();


        // "может просматривать информацию о студенте, если студент из его группы"
        if (tokenData.getRole().equals("ROLE_TEACHER")) {
            StudentInTeachersGroupProjection relation = this.groupRepository.relationBetweenTeacherAndStudentInGroup(
                    tokenData.getEntityId(),
                    studentId
            ).orElse(null);

            if (relation == null)
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // если предыдущие проверки прошли или пользователь оказался ADMIN-ом:
        return this.responseEntityWithPersonProjection(student);

    }

    private ResponseEntity<PersonProjection> responseEntityWithPersonProjection(PersonProjection student) {
        if (student == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(student);
    }

}
