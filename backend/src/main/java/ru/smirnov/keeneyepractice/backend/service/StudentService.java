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
import ru.smirnov.keeneyepractice.backend.dto.user.UserByPersonForUpdateDto;
import ru.smirnov.keeneyepractice.backend.entity.Student;
import ru.smirnov.keeneyepractice.backend.entity.Teacher;
import ru.smirnov.keeneyepractice.backend.entity.auxiliary.Person;
import ru.smirnov.keeneyepractice.backend.exceptions.EntityNotFoundException;
import ru.smirnov.keeneyepractice.backend.mapper.StudentMapper;
import ru.smirnov.keeneyepractice.backend.projection.PersonProjection;
import ru.smirnov.keeneyepractice.backend.projection.StudentByGroupProjection;
import ru.smirnov.keeneyepractice.backend.projection.StudentInTeachersGroupProjection;
import ru.smirnov.keeneyepractice.backend.projection.UserByPersonProjection;
import ru.smirnov.keeneyepractice.backend.repository.GroupRepository;
import ru.smirnov.keeneyepractice.backend.repository.StudentRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService implements RoledEntityService {

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

    public ResponseEntity<List<PersonProjection>> findAllStudents() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


//        if (authentication != null && authentication.getPrincipal() instanceof DataForToken)
//            tokenData = (DataForToken) authentication.getPrincipal();


        final DataForToken tokenData = (DataForToken) authentication.getPrincipal();

        // да-да - супер неоптимальное решение. Надо было бы вообще разделить это на разные эндпоинты
        // и написать ещё и запросы красивые, но для учебного задания для разграничения ролей - пойдёт
        List<StudentByGroupProjection> students = this.studentRepository.findStudents();

        // "может просматривать информацию о всех одногруппниках"
        if (tokenData.getRole().equals("ROLE_STUDENT")) {

            Long groupId = students.stream()
                    .filter(student -> student.getId().equals(tokenData.getEntityId()))
                    .findFirst().orElse(null).getGroupId();

            List<PersonProjection> studentsFromTheSameGroup = students.stream()
                    .filter(student -> student.getGroupId().equals(groupId))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(studentsFromTheSameGroup);
        }

        // "может просматривать информацию о всех студентах из групп, назначенных на него"
        if (tokenData.getRole().equals("ROLE_TEACHER")) {

            return ResponseEntity.ok(
                    students.stream()
                            .filter(student -> student.getTeacherId().equals(tokenData.getEntityId()))
                            .collect(Collectors.toList())
            );

        }

        return ResponseEntity.ok(students.stream().collect(Collectors.toList()));
    }

    private ResponseEntity<PersonProjection> responseEntityWithPersonProjection(PersonProjection student) {
        if (student == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(student);
    }

    public ResponseEntity<OutcomingPersonDto> updateStudentById(Long studentId, IncomingPersonDto dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        DataForToken tokenData = null;

        if (authentication != null && authentication.getPrincipal() instanceof DataForToken)
            tokenData = (DataForToken) authentication.getPrincipal();

        // "изменять информацию только о себе"
        if (tokenData.getRole().equals("ROLE_STUDENT") && !tokenData.getEntityId().equals(studentId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();


        // "редактировать каждого (любого) студента, относящегося к его группе"
        if (tokenData.getRole().equals("ROLE_TEACHER")) {
            StudentInTeachersGroupProjection projection = this.groupRepository.relationBetweenTeacherAndStudentInGroup(
                    tokenData.getEntityId(), studentId
            ).orElse(null);

            if (projection == null)
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Student student = studentRepository.findById(studentId).orElse(null);

        if (student == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        this.studentMapper.updateStudentEntityWithIncomingPersonDto(dto, student);
        this.studentRepository.save(student);

        return ResponseEntity.ok(this.studentMapper.studentEntityToOutcomingPersonDto(student));

    }

    public ResponseEntity<Long> createStudent(IncomingPersonDto dto) {
        /* в силу того, что это просто boilerplate-код, пока не реализовал:
        данный метод в принципе не предполагает разграничение доступа по ролям*/
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    public Long save(Person personToSave) {
        return this.studentRepository.save((Student) personToSave).getId();
    }

    @Override
    public List<UserByPersonProjection> findAll() {
        // return RoledEntityService.super.findAll();
        return this.studentRepository.findStudentsWithUserData();
    }

    @Override
    public Optional<UserByPersonProjection> findById(Long id) {
        return this.studentRepository.findStudentWithUserDataByStudentId(id);
    }

    @Override
    public Person updateById(UserByPersonForUpdateDto dataForUpdate, Long id) throws EntityNotFoundException {
        Student student = this.studentRepository.findById(id).orElse(null);

        if (student == null) throw new EntityNotFoundException("No such entity by this id");

        student.setLastname(dataForUpdate.getLastname());
        student.setFirstname(dataForUpdate.getFirstname());
        student.setParentname(dataForUpdate.getParentname());
        student.setBirthDate(dataForUpdate.getBirthDate());
        student.setPhoneNumber(dataForUpdate.getPhoneNumber());
        student.setEmail(dataForUpdate.getEmail());

        return student;
    }
}
