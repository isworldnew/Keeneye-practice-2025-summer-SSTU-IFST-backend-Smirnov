package ru.smirnov.keeneyepractice.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.smirnov.keeneyepractice.backend.entity.Student;
import ru.smirnov.keeneyepractice.backend.projection.PersonProjection;
import ru.smirnov.keeneyepractice.backend.projection.StudentByGroupProjection;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query(value = "SELECT student.id FROM Student student WHERE student.user.id = :userId")
    Optional<Long> findStudentIdByUserId(Long userId);

    @Query(
            value = """
                    SELECT
                        st.id,
                    	u.username,
                    	st.lastname,
                    	st.firstname,
                    	st.parentname,
                    	st.birth_date,
                    	st.phone_number,
                    	st.email
                    FROM
                        students st
                    JOIN
                        users u ON st.user_id = u.id
                    WHERE
                        st.id = :studentId;
               
                    """,
            nativeQuery = true
    )
    Optional<PersonProjection> findStudentById(@Param("studentId") Long studentId);


    @Query(
            value = """
                    SELECT
                        st.id,
                        u.username,
                        st.lastname,
                        st.firstname,
                        st.parentname,
                        st.birth_date,
                        st.phone_number,
                        st.email,
                        sbg.group_id,
                        g.teacher_id
                    FROM
                        students st
                    JOIN
                        users u ON st.user_id = u.id
                    LEFT JOIN
                        students_by_groups sbg ON sbg.student_id = st.id
                    LEFT JOIN
                        groups g ON sbg.group_id = g.id
                    """,
            nativeQuery = true
    )
    List<StudentByGroupProjection> findStudents();

}
