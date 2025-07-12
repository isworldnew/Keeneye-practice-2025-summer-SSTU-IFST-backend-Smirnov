package ru.smirnov.keeneyepractice.backend.entity.auxiliary;

import org.springframework.stereotype.Component;
import ru.smirnov.keeneyepractice.backend.dto.user.UserToCreateDto;
import ru.smirnov.keeneyepractice.backend.entity.Student;
import ru.smirnov.keeneyepractice.backend.entity.Teacher;
import ru.smirnov.keeneyepractice.backend.entity.User;
import ru.smirnov.keeneyepractice.backend.exceptions.NoSuchRoleException;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class SimplePersonFabric {

    private static Map<String, PersonGenerator> waysOfCreation = Map.of(
            "STUDENT", new PersonGenerator() {
                @Override
                public Person generatePerson(UserToCreateDto dto, User createdUser) {
                    Student student = new Student();
                    student.setLastname(dto.getLastname());
                    student.setFirstname(dto.getFirstname());
                    student.setParentname(dto.getParentname());
                    student.setBirthDate(dto.getBirthDate());
                    student.setPhoneNumber(dto.getPhoneNumber());
                    student.setEmail(dto.getEmail());
                    student.setUser(createdUser);
                    return student;
                }
            },

            "TEACHER", new PersonGenerator() {
                @Override
                public Person generatePerson(UserToCreateDto dto, User createdUser) {
                    Teacher teacher = new Teacher();
                    teacher.setLastname(dto.getLastname());
                    teacher.setFirstname(dto.getFirstname());
                    teacher.setParentname(dto.getParentname());
                    teacher.setBirthDate(dto.getBirthDate());
                    teacher.setPhoneNumber(dto.getPhoneNumber());
                    teacher.setEmail(dto.getEmail());
                    teacher.setUser(createdUser);
                    return teacher;
                }
            },

            "ADMIN", new PersonGenerator() {
                @Override
                public Person generatePerson(UserToCreateDto dto, User createdUser) {
                    throw new NoSuchRoleException("Нельзя создать сущность типа ADMIN");
                }
            }
    );

    public static Person generatePerson(UserToCreateDto dto, User createdUser) {
        PersonGenerator wayOfCreation = waysOfCreation.get(dto.getRole());

        if (wayOfCreation == null)
            throw new NoSuchRoleException("Такой роли не существует");

        return wayOfCreation.generatePerson(dto, createdUser);
    }

    @FunctionalInterface
    private interface PersonGenerator {
        Person generatePerson (UserToCreateDto dto, User createdUser);
    }

}
