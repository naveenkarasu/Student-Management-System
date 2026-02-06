package com.studentmgmt.controller;

import com.studentmgmt.dto.EnrollmentRequest;
import com.studentmgmt.model.Enrollment;
import com.studentmgmt.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllEnrollments() {
        List<Map<String, Object>> enrollments = enrollmentService.getAllEnrollments().stream()
                .map(this::toMap)
                .toList();
        return ResponseEntity.ok(enrollments);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createEnrollment(@Valid @RequestBody EnrollmentRequest request) {
        Enrollment enrollment = enrollmentService.createEnrollment(request);
        return ResponseEntity.ok(toMap(enrollment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Map<String, Object>>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        List<Map<String, Object>> enrollments = enrollmentService.getEnrollmentsByStudent(studentId).stream()
                .map(this::toMap)
                .toList();
        return ResponseEntity.ok(enrollments);
    }

    private Map<String, Object> toMap(Enrollment enrollment) {
        return Map.of(
                "enrollmentId", enrollment.getEnrollmentId(),
                "studentId", enrollment.getStudent().getStudentId(),
                "studentName", enrollment.getStudent().getName(),
                "courseId", enrollment.getCourse().getCourseId(),
                "courseCode", enrollment.getCourse().getCourseCode(),
                "courseName", enrollment.getCourse().getCourseName(),
                "semester", enrollment.getSemester() != null ? enrollment.getSemester() : "",
                "enrolledAt", enrollment.getEnrolledAt() != null ? enrollment.getEnrolledAt().toString() : ""
        );
    }
}
