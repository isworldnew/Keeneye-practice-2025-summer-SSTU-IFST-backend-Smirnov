package ru.smirnov.keeneyepractice.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.smirnov.keeneyepractice.backend.entity.Teacher;
import ru.smirnov.keeneyepractice.backend.projection.PersonProjection;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @Query("SELECT teacher.id FROM Teacher teacher WHERE teacher.user.id = :userId")
    Optional<Long> findTeacherIdByUserId(Long userId);

    @Query(
            value = """
                    SELECT
                        t.id,
                    	u.username,
                    	t.lastname,
                    	t.firstname,
                    	t.parentname,
                    	t.birth_date,
                    	t.phone_number,
                    	t.email
                    FROM
                        teachers t
                    JOIN
                        users u ON t.user_id = u.id
                    WHERE
                        t.id = :teacherId;
               
                    """,
            nativeQuery = true
    )
    Optional<PersonProjection> findTeacherById(@Param("teacherId") Long teacherId);

}
