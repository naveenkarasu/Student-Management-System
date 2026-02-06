package com.studentmgmt.service;

import com.studentmgmt.dto.GradeRequest;
import com.studentmgmt.dto.GradeResponse;
import com.studentmgmt.model.Enrollment;
import com.studentmgmt.model.Grade;
import com.studentmgmt.repository.EnrollmentRepository;
import com.studentmgmt.repository.GradeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;
    private final EnrollmentRepository enrollmentRepository;

    public GradeService(GradeRepository gradeRepository, EnrollmentRepository enrollmentRepository) {
        this.gradeRepository = gradeRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    @Transactional
    public Grade assignGrade(GradeRequest request) {
        Enrollment enrollment = enrollmentRepository.findById(request.getEnrollmentId())
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + request.getEnrollmentId()));

        Grade grade = gradeRepository.findByEnrollment(enrollment).orElse(new Grade());
        grade.setEnrollment(enrollment);
        grade.setMarks(request.getMarks());

        String letterGrade = request.getLetterGrade();
        if (letterGrade == null || letterGrade.isBlank()) {
            letterGrade = calculateLetterGrade(request.getMarks());
        }
        grade.setGrade(letterGrade);

        return gradeRepository.save(grade);
    }

    @Transactional
    public Grade updateGrade(Long gradeId, GradeRequest request) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new RuntimeException("Grade not found with id: " + gradeId));

        grade.setMarks(request.getMarks());

        String letterGrade = request.getLetterGrade();
        if (letterGrade == null || letterGrade.isBlank()) {
            letterGrade = calculateLetterGrade(request.getMarks());
        }
        grade.setGrade(letterGrade);

        return gradeRepository.save(grade);
    }

    public List<Grade> getGradesByStudent(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    public List<Grade> getGradesByStudentAndSemester(Long studentId, String semester) {
        return gradeRepository.findByStudentIdAndSemester(studentId, semester);
    }

    public double calculateGPA(Long studentId) {
        List<Grade> grades = gradeRepository.findByStudentId(studentId);
        return computeWeightedGPA(grades);
    }

    public double calculateSemesterGPA(Long studentId, String semester) {
        List<Grade> grades = gradeRepository.findByStudentIdAndSemester(studentId, semester);
        return computeWeightedGPA(grades);
    }

    private double computeWeightedGPA(List<Grade> grades) {
        if (grades.isEmpty()) {
            return 0.0;
        }

        double totalWeightedPoints = 0.0;
        int totalCredits = 0;

        for (Grade g : grades) {
            int credits = g.getEnrollment().getCourse().getCredits();
            totalWeightedPoints += credits * g.getGradePoints();
            totalCredits += credits;
        }

        if (totalCredits == 0) {
            return 0.0;
        }

        return Math.round((totalWeightedPoints / totalCredits) * 100.0) / 100.0;
    }

    public String calculateLetterGrade(double marks) {
        if (marks >= 90) return "A+";
        if (marks >= 85) return "A";
        if (marks >= 80) return "A-";
        if (marks >= 75) return "B+";
        if (marks >= 70) return "B";
        if (marks >= 65) return "B-";
        if (marks >= 60) return "C+";
        if (marks >= 55) return "C";
        if (marks >= 50) return "C-";
        if (marks >= 45) return "D+";
        if (marks >= 40) return "D";
        return "F";
    }

    public GradeResponse toResponse(Grade grade) {
        GradeResponse response = new GradeResponse();
        response.setGradeId(grade.getGradeId());
        response.setEnrollmentId(grade.getEnrollment().getEnrollmentId());
        response.setCourseCode(grade.getEnrollment().getCourse().getCourseCode());
        response.setCourseName(grade.getEnrollment().getCourse().getCourseName());
        response.setCredits(grade.getEnrollment().getCourse().getCredits());
        response.setMarks(grade.getMarks());
        response.setLetterGrade(grade.getGrade());
        response.setGradePoints(grade.getGradePoints());
        response.setSemester(grade.getEnrollment().getSemester());
        return response;
    }
}
