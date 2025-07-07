package ru.smirnov.keeneyepractice.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.smirnov.keeneyepractice.backend.dto.GroupDto;
import ru.smirnov.keeneyepractice.backend.entity.Group;
import ru.smirnov.keeneyepractice.backend.entity.UserEntity;
import ru.smirnov.keeneyepractice.backend.repository.GroupRepository;
import ru.smirnov.keeneyepractice.backend.repository.UserRepository;
import ru.smirnov.keeneyepractice.backend.entity.Role;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<Long> createGroup(GroupDto dto) {
        Group group = new Group();
        group.setName(dto.getName());

        if (dto.getTeacherId() != null) {
            UserEntity teacher = userRepository.findById(dto.getTeacherId())
                    .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
            if (teacher.getRole() != Role.TEACHER) {
                throw new IllegalArgumentException("User is not a teacher");
            }
            group.setTeacher(teacher);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(
                groupRepository.save(group).getId()
        );
    }

    public ResponseEntity<List<Group>> getAllGroups() {
        return ResponseEntity.ok(groupRepository.findAll());
    }

    public ResponseEntity<Group> getGroupById(Long id) {
        return groupRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Group> updateGroup(Long id, GroupDto dto) {
        return groupRepository.findById(id)
                .map(group -> {
                    group.setName(dto.getName());
                    if (dto.getTeacherId() != null) {
                        UserEntity teacher = userRepository.findById(dto.getTeacherId())
                                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
                        if (teacher.getRole() != Role.TEACHER) {
                            throw new IllegalArgumentException("User is not a teacher");
                        }
                        group.setTeacher(teacher);
                    } else {
                        group.setTeacher(null);
                    }
                    return ResponseEntity.ok(groupRepository.save(group));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Void> deleteGroup(Long id) {
        if (groupRepository.existsById(id)) {
            groupRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<List<Group>> getGroupsByTeacher(Long teacherId) {
        return ResponseEntity.ok(groupRepository.findByTeacherId(teacherId));
    }
}