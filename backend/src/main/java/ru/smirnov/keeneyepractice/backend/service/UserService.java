package ru.smirnov.keeneyepractice.backend.service;

import jakarta.transaction.Transactional;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.smirnov.keeneyepractice.backend.dto.authentication.DataForToken;
import ru.smirnov.keeneyepractice.backend.dto.user.CreatedUserDataDto;
import ru.smirnov.keeneyepractice.backend.dto.user.UserToCreateDto;
import ru.smirnov.keeneyepractice.backend.entity.Student;
import ru.smirnov.keeneyepractice.backend.entity.Teacher;
import ru.smirnov.keeneyepractice.backend.entity.User;
import ru.smirnov.keeneyepractice.backend.entity.auxiliary.Person;
import ru.smirnov.keeneyepractice.backend.entity.auxiliary.Role;
import ru.smirnov.keeneyepractice.backend.entity.auxiliary.RoleManager;
import ru.smirnov.keeneyepractice.backend.exceptions.NoSuchRoleException;
import ru.smirnov.keeneyepractice.backend.exceptions.ServiceMethodNotImplementedException;
import ru.smirnov.keeneyepractice.backend.mapper.UserMapper;
import ru.smirnov.keeneyepractice.backend.repository.StudentRepository;
import ru.smirnov.keeneyepractice.backend.repository.TeacherRepository;
import ru.smirnov.keeneyepractice.backend.repository.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final UserMapper userMapper;


    private final RoleManager roleManager;

    @Autowired
    public UserService(
            UserRepository userRepository,
            TeacherService teacherService,
            StudentService studentService,
            UserMapper userMapper,
            StudentRepository studentRepository,
            TeacherRepository teacherRepository,

            RoleManager roleManager
    ) {
        this.userRepository = userRepository;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.userMapper = userMapper;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;

        this.roleManager = roleManager;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = this.userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Пользователь не найден")
        );

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name()) // ВОТ ТУТ ИЗМЕНИЛ: добавил "ROLE_" +
        );

        Long entityId = null; // базово считаем, что она null, будто это ADMIN
        Long teacherId = this.teacherService.findTeacherIdByUserId(user.getId()).orElse(null);
        Long studentId = this.studentService.findStudentIdByUserId(user.getId()).orElse(null);

        if (teacherId != null) entityId = teacherId;

        if (studentId != null) entityId = studentId;

        DataForToken dataForToken = DataForToken.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .enabled(user.isEnabled())
                .authorities(authorities)
                .userId(user.getId())
                .role("ROLE_" + user.getRole().toString()) // для @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
                .entityId(entityId)
                .build();

        System.out.println("DATA: " + dataForToken.toString());

        return dataForToken;
    }



    // методы для обычных эндпоинтов:

    @Transactional
    @Deprecated
    public ResponseEntity<CreatedUserDataDto> createUserWithBusinessDataDeprecated(UserToCreateDto dto) {

        // обязательно проверить, что username - уникальный
        List<User> existingUsers = this.userRepository.findAll();

        boolean usernameAlreadyExists = existingUsers.stream()
                .map(user -> user.getUsername())
                .anyMatch(username -> username.equals(dto.getUsername()));

        if (usernameAlreadyExists)
            return ResponseEntity.status(HttpStatus.CONFLICT).build();


        // сначала сохранить user-а и не забыть зашифровать пароль
        User userToCreate = new User();
        userToCreate.setUsername(dto.getUsername());
        userToCreate.setPassword(User.encodeRawPassword(dto.getRawPassword()));
        userToCreate.setRole(Role.valueOf(dto.getRole()));
        userToCreate.setEnabled(dto.getEnabled());

        userToCreate = this.userRepository.save(userToCreate);

        // хорошо бы тут каждой роли enum-а задать метод, который обращал бы её к своей БД, но пока напишу не ООП-шно
        // типа... просто передаёшь константу, она сама там своим методом пользуется

        Person personToCreate;
        Long createdPersonId;

        // потом создать с User id бизнесовую логику в одной из таблиц

        if (dto.getRole().equals("STUDENT")) {
            personToCreate = new Student();
            createPerson(personToCreate, dto, userToCreate);
            createdPersonId = this.studentRepository.save((Student) personToCreate).getId();
        }

        else {
            personToCreate = new Teacher();
            createPerson(personToCreate, dto, userToCreate);
            createdPersonId = this.teacherRepository.save((Teacher) personToCreate).getId();
        }


        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CreatedUserDataDto(createdPersonId, userToCreate.getId())
        );

    }


    @Transactional
    public ResponseEntity<CreatedUserDataDto> createUserWithBusinessData(UserToCreateDto dto) {
        List<User> existingUsers = this.userRepository.findAll();

        boolean usernameAlreadyExists = existingUsers.stream()
                .map(user -> user.getUsername())
                .anyMatch(username -> username.equals(dto.getUsername()));

        if (usernameAlreadyExists)
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        User createdUser = this.userRepository.save(this.initializeUser(dto));


        Student student = new Student();
        student.setLastname(dto.getLastname());
        student.setFirstname(dto.getFirstname());
        student.setParentname(dto.getParentname());
        student.setBirthDate(dto.getBirthDate());
        student.setPhoneNumber(dto.getPhoneNumber());
        student.setEmail(dto.getEmail());

        Teacher teacher = new Teacher();
        teacher.setLastname(dto.getLastname());
        teacher.setFirstname(dto.getFirstname());
        teacher.setParentname(dto.getParentname());
        teacher.setBirthDate(dto.getBirthDate());
        teacher.setPhoneNumber(dto.getPhoneNumber());
        teacher.setEmail(dto.getEmail());

        Person person = null;

        if (dto.getRole().equals("STUDENT"))
            person = student;

        if (dto.getRole().equals("TEACHER"))
            person = teacher;


//        try {
            Long createdEntityId = this.roleManager.valueOf(dto.getRole()).save(
                    person /*сюда бы абстрактную фабрику или фабричный метод*/
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new CreatedUserDataDto(createdEntityId, createdUser.getId())
            );
//        }
//        catch (NoSuchRoleException nsre) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//        catch (ServiceMethodNotImplementedException smnie) {
//            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
//        }


    }

    private User initializeUser(UserToCreateDto dto) {
        User userToCreate = new User();
        userToCreate.setUsername(dto.getUsername());
        userToCreate.setPassword(User.encodeRawPassword(dto.getRawPassword()));
        userToCreate.setRole(Role.valueOf(dto.getRole()));
        userToCreate.setEnabled(dto.getEnabled());
        return userToCreate;
    }

    private void createPerson(Person personToCreate, UserToCreateDto dto, User createdUser) {
        personToCreate.setFirstname(dto.getFirstname());
        personToCreate.setLastname(dto.getLastname());
        personToCreate.setParentname(dto.getParentname());
        personToCreate.setBirthDate(dto.getBirthDate());
        personToCreate.setPhoneNumber(dto.getPhoneNumber());
        personToCreate.setEmail(dto.getEmail());
        personToCreate.setUser(createdUser);
    }

}
