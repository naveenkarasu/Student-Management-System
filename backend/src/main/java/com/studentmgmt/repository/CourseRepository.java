package com.studentmgmt.repository;

import com.studentmgmt.model.Course;
import com.studentmgmt.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseCode(String courseCode);
    List<Course> findByFaculty(Faculty faculty);
    boolean existsByCourseCode(String courseCode);
}
