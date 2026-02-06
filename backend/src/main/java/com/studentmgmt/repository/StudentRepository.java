package com.studentmgmt.repository;

import com.studentmgmt.model.Student;
import com.studentmgmt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByRollNumber(String rollNumber);
    Optional<Student> findByUser(User user);
    boolean existsByRollNumber(String rollNumber);
}
