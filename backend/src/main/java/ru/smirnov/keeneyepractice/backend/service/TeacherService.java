package ru.smirnov.keeneyepractice.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.smirnov.keeneyepractice.backend.dto.authentication.DataForToken;
import ru.smirnov.keeneyepractice.backend.dto.basic.IncomingPersonDto;
import ru.smirnov.keeneyepractice.backend.dto.basic.OutcomingPersonDto;
import ru.smirnov.keeneyepractice.backend.entity.Student;
import ru.smirnov.keeneyepractice.backend.entity.Teacher;
import ru.smirnov.keeneyepractice.backend.entity.auxiliary.Person;
import ru.smirnov.keeneyepractice.backend.mapper.TeacherMapper;
import ru.smirnov.keeneyepractice.backend.projection.PersonProjection;
import ru.smirnov.keeneyepractice.backend.projection.UserByPersonProjection;
import ru.smirnov.keeneyepractice.backend.repository.TeacherRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService implements RoledEntityService {

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

        if (tokenData.getRole().equals("ROLE_STUDENT"))
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();

        // если не ADMIN и id не совпали - нельзя обращаться
        if (!tokenData.getRole().equals("ROLE_ADMIN") && !tokenData.getEntityId().equals(teacherId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        PersonProjection teacher = this.teacherRepository.findTeacherById(teacherId).orElse(null);

        if (teacher == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(teacher);
    }

    public ResponseEntity<Long> createTeacher(IncomingPersonDto dto) {
        /* в силу того, что это просто boilerplate-код, пока не реализовал:
        данный метод в принципе не предполагает разграничение доступа по ролям*/
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    public ResponseEntity<List<OutcomingPersonDto>> findTeachers() {
        /* в силу того, что это просто boilerplate-код, пока не реализовал:
        данный метод в принципе не предполагает разграничение доступа по ролям*/
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    public ResponseEntity<OutcomingPersonDto> updateTeacherById(Long id, IncomingPersonDto dto) {
        /* в силу того, что это просто boilerplate-код, пока не реализовал:
        данный метод в принципе не предполагает разграничение доступа по ролям*/
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }


    @Override
    public Long save(Person personToSave) {
        return this.teacherRepository.save((Teacher) personToSave).getId();
    }

    @Override
    public List<UserByPersonProjection> findAll() {
        return this.teacherRepository.findTeachersWithUserData();
    }

    @Override
    public Optional<UserByPersonProjection> findById(Long id) {
        return this.teacherRepository.findTeacherWithUserDataByTeacherId(id);
    }
}
