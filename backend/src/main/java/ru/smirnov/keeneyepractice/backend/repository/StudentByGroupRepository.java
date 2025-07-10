package ru.smirnov.keeneyepractice.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smirnov.keeneyepractice.backend.entity.StudentByGroup;

@Repository
public interface StudentByGroupRepository extends JpaRepository<StudentByGroup, Long> {
}
