package ru.smirnov.keeneyepractice.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.smirnov.keeneyepractice.backend.entity.Group;
import ru.smirnov.keeneyepractice.backend.projection.StudentInTeachersGroupProjection;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query(
            value = """
                    SELECT
                    	teachers.id AS teacherId,
                    	groups.id AS groupId,
                    	students_by_groups.student_id AS studentId
                    FROM
                    	teachers
                    JOIN
                    	groups ON groups.teacher_id = teachers.id
                    JOIN
                    	students_by_groups ON students_by_groups.group_id = groups.id
                    JOIN
                    	students ON students.id = students_by_groups.student_id
                    WHERE
                    	teachers.id = :teacherId AND students.id = :studentId
                    """,
            nativeQuery = true
    )
    Optional<StudentInTeachersGroupProjection> relationBetweenTeacherAndStudentInGroup(
            @Param("teacherId") Long teacherId,
            @Param("studentId") Long studentId
    );

}
