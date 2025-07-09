package ru.smirnov.keeneyepractice.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.keeneyepractice.backend.mapper.UserMapper;
import ru.smirnov.keeneyepractice.backend.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
}
