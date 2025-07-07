/*package ru.smirnov.keeneyepractice.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.smirnov.keeneyepractice.backend.dto.IncomingStudentDto;
import ru.smirnov.keeneyepractice.backend.entity.Role;
import ru.smirnov.keeneyepractice.backend.entity.PasswordEntity;
import ru.smirnov.keeneyepractice.backend.entity.UserEntity;
import ru.smirnov.keeneyepractice.backend.repository.UserRepository;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class InitializerService {

    private final StudentService studentService;
    private final UserRepository userRepository;

    public void initial() {
        studentService.createStudent(new IncomingStudentDto(
                "Смирнов", "Иван", "Вадимович",
                LocalDate.of(2003, 11, 2),
                "58391034839", "ivans@example.com", "Группа-101"
        ));

        studentService.createStudent(new IncomingStudentDto(
                "Гудков", "Петр", null,
                LocalDate.of(2001, 5, 15),
                "94721350943", "petrgv@example.com", "Группа-102"
        ));

        try {
            PasswordEntity adminPassword = new PasswordEntity("admin123");
            PasswordEntity studentPassword = new PasswordEntity("student123");

            UserEntity admin = UserEntity.builder()
                    .username("admin")
                    .passwordEntity(adminPassword)
                    .enabled(true)
                    .role(Role.ADMIN)
                    .build();

            UserEntity student = UserEntity.builder()
                    .username("student")
                    .passwordEntity(studentPassword)
                    .enabled(true)
                    .role(Role.STUDENT)
                    .build();

            userRepository.save(admin);
            userRepository.save(student);

        } catch (Exception e) {
            System.err.println("Ошибка при инициализации тестовых пользователей: " + e.getMessage());
        }
    }
}*/