package ru.smirnov.keeneyepractice.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.smirnov.keeneyepractice.backend.entity.Student;
import ru.smirnov.keeneyepractice.backend.projection.PersonProjection;

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

}
