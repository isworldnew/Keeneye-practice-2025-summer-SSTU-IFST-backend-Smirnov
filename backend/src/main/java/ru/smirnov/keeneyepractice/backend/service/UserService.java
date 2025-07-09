package ru.smirnov.keeneyepractice.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.smirnov.keeneyepractice.backend.dto.authorization.DataForToken;
import ru.smirnov.keeneyepractice.backend.entity.User;
import ru.smirnov.keeneyepractice.backend.mapper.UserMapper;
import ru.smirnov.keeneyepractice.backend.repository.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final UserMapper userMapper;

    @Autowired
    public UserService(
            UserRepository userRepository,
            TeacherService teacherService,
            StudentService studentService,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = this.userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Пользователь не найден")
        );

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().name())
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
                .role(user.getRole().toString())
                .entityId(entityId)
                .build();

        System.out.println("DATA: " + dataForToken.toString());

        return dataForToken;
    }

}
