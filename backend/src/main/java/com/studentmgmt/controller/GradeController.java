package com.studentmgmt.controller;

import com.studentmgmt.dto.GradeRequest;
import com.studentmgmt.dto.GradeResponse;
import com.studentmgmt.model.Grade;
import com.studentmgmt.service.GradeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/grades")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping
    public ResponseEntity<List<GradeResponse>> getAllGrades() {
        List<GradeResponse> grades = gradeService.getAllGrades().stream()
                .map(gradeService::toResponse)
                .toList();
        return ResponseEntity.ok(grades);
    }

    @PostMapping
    public ResponseEntity<GradeResponse> assignGrade(@Valid @RequestBody GradeRequest request) {
        Grade grade = gradeService.assignGrade(request);
        return ResponseEntity.ok(gradeService.toResponse(grade));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GradeResponse> updateGrade(@PathVariable Long id, @Valid @RequestBody GradeRequest request) {
        Grade grade = gradeService.updateGrade(id, request);
        return ResponseEntity.ok(gradeService.toResponse(grade));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<GradeResponse>> getGradesByStudent(@PathVariable Long studentId) {
        List<GradeResponse> grades = gradeService.getGradesByStudent(studentId).stream()
                .map(gradeService::toResponse)
                .toList();
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/student/{studentId}/semester/{semester}")
    public ResponseEntity<List<GradeResponse>> getGradesByStudentAndSemester(
            @PathVariable Long studentId, @PathVariable String semester) {
        List<GradeResponse> grades = gradeService.getGradesByStudentAndSemester(studentId, semester).stream()
                .map(gradeService::toResponse)
                .toList();
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/student/{studentId}/gpa")
    public ResponseEntity<Map<String, Object>> getStudentGPA(@PathVariable Long studentId) {
        double gpa = gradeService.calculateGPA(studentId);
        return ResponseEntity.ok(Map.of(
                "studentId", studentId,
                "cumulativeGPA", gpa
        ));
    }
}
