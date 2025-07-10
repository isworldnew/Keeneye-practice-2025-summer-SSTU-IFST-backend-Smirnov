package ru.smirnov.keeneyepractice.backend.mapper;

import org.springframework.stereotype.Component;
import ru.smirnov.keeneyepractice.backend.dto.basic.IncomingPersonDto;
import ru.smirnov.keeneyepractice.backend.dto.basic.OutcomingPersonDto;
import ru.smirnov.keeneyepractice.backend.entity.Student;

@Component
public class StudentMapper {

    public void updateStudentEntityWithIncomingPersonDto(IncomingPersonDto dto, Student student) {
        student.setLastname(dto.getLastname());
        student.setFirstname(dto.getFirstname());
        student.setParentname(dto.getParentname());
        student.setBirthDate(dto.getBirthDate());
        student.setPhoneNumber(dto.getPhoneNumber());
        student.setEmail(dto.getEmail());
    }

    public OutcomingPersonDto studentEntityToOutcomingPersonDto(Student student) {
        OutcomingPersonDto dto = new OutcomingPersonDto();
        dto.setId(student.getId());
        dto.setLastname(student.getLastname());
        dto.setFirstname(student.getFirstname());
        dto.setParentname(student.getParentname());
        dto.setBirthDate(student.getBirthDate());
        dto.setPhoneNumber(student.getPhoneNumber());
        dto.setEmail(student.getEmail());
        return dto;
    }
}
