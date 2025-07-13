package ru.smirnov.keeneyepractice.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.smirnov.keeneyepractice.backend.entity.Teacher;
import ru.smirnov.keeneyepractice.backend.projection.PersonProjection;
import ru.smirnov.keeneyepractice.backend.projection.UserByPersonProjection;

import java.util.List;
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

    @Query(
            value = """
                    SELECT
                    	teachers.id AS id,
                    	teachers.user_id AS userId,
                    	teachers.lastname AS lastname,
                    	teachers.firstname AS firstname,
                    	teachers.parentname AS parentname,
                    	teachers.birth_date AS birthDate,
                    	teachers.phone_number AS phoneNumber,
                    	teachers.email AS email,	
                    	users.username AS username,
                    	users.role AS role,
                    	users.enabled AS enabled
                    FROM teachers
                    JOIN users
                    ON teachers.user_id = users.id
                    ORDER BY teachers.id;
                    """,
            nativeQuery = true
    )
    List<UserByPersonProjection> findTeachersWithUserData();


    @Query(
            value = """
                    SELECT
                    	teachers.id AS id,
                    	teachers.user_id AS userId,
                    	teachers.lastname AS lastname,
                    	teachers.firstname AS firstname,
                    	teachers.parentname AS parentname,
                    	teachers.birth_date AS birthDate,
                    	teachers.phone_number AS phoneNumber,
                    	teachers.email AS email,	
                    	users.username AS username,
                    	users.role AS role,
                    	users.enabled AS enabled
                    FROM teachers
                    JOIN users
                    ON teachers.user_id = users.id
                    WHERE teachers.id = :teacherId;
                    """,
            nativeQuery = true
    )
    Optional<UserByPersonProjection> findTeacherWithUserDataByTeacherId(@Param("teacherId") Long teacherId);

}
