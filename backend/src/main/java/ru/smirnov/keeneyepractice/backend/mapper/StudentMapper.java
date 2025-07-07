package ru.smirnov.keeneyepractice.backend.mapper;

import org.springframework.stereotype.Component;
import ru.smirnov.keeneyepractice.backend.dto.IncomingStudentDto;
import ru.smirnov.keeneyepractice.backend.entity.Group;
import ru.smirnov.keeneyepractice.backend.entity.Student;
import ru.smirnov.keeneyepractice.backend.repository.GroupRepository;

@Component
public class StudentMapper {

    private final GroupRepository groupRepository;

    public StudentMapper(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Student incomingStudentDtoToStudentEntity(IncomingStudentDto dto) {
        Student student = new Student();
        student.setLastname(dto.getLastname());
        student.setFirstname(dto.getFirstname());
        student.setParentname(dto.getParentname());
        student.setBirthDate(dto.getBirthDate());
        student.setPhoneNumber(dto.getPhoneNumber());
        student.setEmail(dto.getEmail());

        Group group = groupRepository.findByName(dto.getGroupName())
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        student.setGroup(group);

        return student;
    }

    public void updateStudent(Student entityToUpdate, IncomingStudentDto modifiedData) {
        entityToUpdate.setLastname(modifiedData.getLastname());
        entityToUpdate.setFirstname(modifiedData.getFirstname());
        entityToUpdate.setParentname(modifiedData.getParentname());
        entityToUpdate.setBirthDate(modifiedData.getBirthDate());
        entityToUpdate.setPhoneNumber(modifiedData.getPhoneNumber());
        entityToUpdate.setEmail(modifiedData.getEmail());

        Group group = groupRepository.findByName(modifiedData.getGroupName())
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        entityToUpdate.setGroup(group);
    }
}