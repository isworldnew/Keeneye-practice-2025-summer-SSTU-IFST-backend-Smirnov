package ru.smirnov.keeneyepractice.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.smirnov.keeneyepractice.backend.entity.Student;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT student.id FROM Student student WHERE student.user.id = :userId")
    Optional<Long> findStudentIdByUserId(Long userId);

}
