package ru.smirnov.keeneyepractice.backend.service;

import jakarta.transaction.Transactional;
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
import ru.smirnov.keeneyepractice.backend.dto.user.UpdatedUserByPersonDto;
import ru.smirnov.keeneyepractice.backend.dto.user.UserByPersonForUpdateDto;
import ru.smirnov.keeneyepractice.backend.dto.user.UserToCreateDto;
import ru.smirnov.keeneyepractice.backend.entity.User;
import ru.smirnov.keeneyepractice.backend.entity.auxiliary.Person;
import ru.smirnov.keeneyepractice.backend.entity.auxiliary.Role;
import ru.smirnov.keeneyepractice.backend.entity.auxiliary.RoleManager;
import ru.smirnov.keeneyepractice.backend.entity.auxiliary.SimplePersonFabric;
import ru.smirnov.keeneyepractice.backend.exceptions.NoSuchRoleException;
import ru.smirnov.keeneyepractice.backend.mapper.UserMapper;
import ru.smirnov.keeneyepractice.backend.projection.UserByPersonProjection;
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


    private final RoleManager roleManager;

    @Autowired
    public UserService(
            UserRepository userRepository,
            TeacherService teacherService,
            StudentService studentService,
            RoleManager roleManager
    ) {
        this.userRepository = userRepository;
        this.teacherService = teacherService;
        this.studentService = studentService;
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
    public ResponseEntity<CreatedUserDataDto> createUserWithBusinessData(UserToCreateDto dto) {
        List<User> existingUsers = this.userRepository.findAll();

        boolean usernameAlreadyExists = existingUsers.stream()
                .map(user -> user.getUsername())
                .anyMatch(username -> username.equals(dto.getUsername()));

        if (usernameAlreadyExists)
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        User createdUser = this.userRepository.save(this.initializeUser(dto));

        Long createdEntityId = this.roleManager.valueOf(dto.getRole()).save(
                SimplePersonFabric.generatePerson(dto, createdUser)
        );
            
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CreatedUserDataDto(createdEntityId, createdUser.getId())
        );

    }

    @Transactional
    public ResponseEntity<UpdatedUserByPersonDto> updateUserWithBusinessData(
            UserByPersonForUpdateDto dto,
            String role, Long entityId
    ) {
         /*
         может... запретить обновлять роль?
         просто, в таком случае у мне придётся удалять запись в одной из таблиц сущностей, и переносить её в другую
         а если удалять, то там поплывут таблицы, где id преподавателя или студента - внешние ключи
         либо ходить ещё в каждую таблицу, обновляя данные уже там (пихая вместо старого id - новый из таблицы сущностей)
         */

        User user = this.userRepository.findByUsername(dto.getOldUsername()).orElse(null);

        User alreadyNamedWithSuchUsername = this.userRepository.findByUsername(dto.getNewUsername()).orElse(null);

        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        if (!user.getPassword().equals(User.encodeRawPassword(dto.getRawOldPassword())))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        if (alreadyNamedWithSuchUsername != null)
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        updateUser(user, dto);

        this.userRepository.save(user);

        Person person = this.roleManager.valueOf(role).updateById(dto, entityId);

        return ResponseEntity.ok(
                new UpdatedUserByPersonDto(
                        person.getId(),
                        user.getId(),
                        person.getLastname(),
                        person.getFirstname(),
                        person.getParentname(),
                        person.getBirthDate(),
                        person.getPhoneNumber(),
                        person.getEmail(),
                        user.getUsername(),
                        user.getRole().toString(),
                        user.isEnabled()
                )
        );

    }

    public ResponseEntity<List<UserByPersonProjection>> findAllUsersAndEntitiesByRole(String role) {

        return ResponseEntity.ok(
                this.roleManager.valueOf(role).findAll()
        );

    }

    public ResponseEntity<UserByPersonProjection> findUserAndEntityByRoleAndEntityId(String role, Long entityId) {

        UserByPersonProjection userToFind = this.roleManager.valueOf(role).findById(entityId).orElse(null);

        if (userToFind == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok(userToFind);

    }


    private User initializeUser(UserToCreateDto dto) {
        User userToCreate = new User();
        userToCreate.setUsername(dto.getUsername());
        userToCreate.setPassword(User.encodeRawPassword(dto.getRawPassword()));
        try {
            userToCreate.setRole(Role.valueOf(dto.getRole()));
        }
        catch (IllegalArgumentException iae) {
            throw new NoSuchRoleException("Invalid role name: " + dto.getRole());
        }
        userToCreate.setEnabled(dto.getEnabled());
        return userToCreate;
    }

    private void updateUser(User userToUpdate, UserByPersonForUpdateDto dto) {
        userToUpdate.setUsername(dto.getNewUsername());
        userToUpdate.setPassword(User.encodeRawPassword(dto.getRawNewPassword()));
        userToUpdate.setEnabled(dto.getEnabled());
    }

}
