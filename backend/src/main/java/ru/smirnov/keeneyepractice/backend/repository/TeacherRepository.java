package ru.smirnov.keeneyepractice.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.smirnov.keeneyepractice.backend.entity.Teacher;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @Query("SELECT teacher.id FROM Teacher teacher WHERE teacher.user.id = :userId")
    Optional<Long> findTeacherIdByUserId(Long userId);

}
