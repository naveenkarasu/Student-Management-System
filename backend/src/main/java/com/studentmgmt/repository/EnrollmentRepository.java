package com.studentmgmt.repository;

import com.studentmgmt.model.Enrollment;
import com.studentmgmt.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent(Student student);
    List<Enrollment> findByStudentAndSemester(Student student, String semester);
    boolean existsByStudentStudentIdAndCourseCourseIdAndSemester(Long studentId, Long courseId, String semester);
}
