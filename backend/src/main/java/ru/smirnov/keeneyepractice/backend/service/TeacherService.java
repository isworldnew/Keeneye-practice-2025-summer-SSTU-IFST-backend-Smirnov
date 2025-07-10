package ru.smirnov.keeneyepractice.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.smirnov.keeneyepractice.backend.dto.authorization.DataForToken;
import ru.smirnov.keeneyepractice.backend.mapper.TeacherMapper;
import ru.smirnov.keeneyepractice.backend.projection.PersonProjection;
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

    public ResponseEntity<PersonProjection> findTeacherById(Long teacherId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        DataForToken tokenData = null;

        if (auth != null && auth.getPrincipal() instanceof DataForToken)
            tokenData = (DataForToken) auth.getPrincipal();

        // если не ADMIN и id не совпали - нельзя обращаться
        if (!tokenData.getRole().equals("ROLE_ADMIN") && !tokenData.getEntityId().equals(teacherId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        PersonProjection teacher = this.teacherRepository.findTeacherById(teacherId).orElse(null);

        if (teacher == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(teacher);
    }

}
