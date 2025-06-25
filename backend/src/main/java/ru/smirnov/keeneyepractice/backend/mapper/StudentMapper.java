package ru.smirnov.keeneyepractice.backend.mapper;

import org.springframework.stereotype.Component;
import ru.smirnov.keeneyepractice.backend.dto.IncomingStudentDto;
import ru.smirnov.keeneyepractice.backend.entity.Student;

@Component
public class StudentMapper {

    public Student incomingStudentDtoToStudentEntity(IncomingStudentDto dto) {
        Student student = new Student();
        student.setLastname(dto.getLastname());
        student.setFirstname(dto.getFirstname());
        student.setParentname(dto.getParentname());
        student.setBirthDate(dto.getBirthDate());
        student.setPhoneNumber(dto.getPhoneNumber());
        student.setEmail(dto.getEmail());
        student.setGroup(dto.getGroup());
        return student;
    }


}
