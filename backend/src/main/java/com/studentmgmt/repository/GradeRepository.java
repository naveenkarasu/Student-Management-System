package com.studentmgmt.repository;

import com.studentmgmt.model.Enrollment;
import com.studentmgmt.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    Optional<Grade> findByEnrollment(Enrollment enrollment);

    @Query("SELECT g FROM Grade g WHERE g.enrollment.student.studentId = :studentId")
    List<Grade> findByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT g FROM Grade g WHERE g.enrollment.student.studentId = :studentId AND g.enrollment.semester = :semester")
    List<Grade> findByStudentIdAndSemester(@Param("studentId") Long studentId, @Param("semester") String semester);
}
