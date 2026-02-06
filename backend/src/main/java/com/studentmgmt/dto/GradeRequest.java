package com.studentmgmt.dto;

import jakarta.validation.constraints.NotNull;

public class GradeRequest {
    @NotNull(message = "Enrollment ID is required")
    private Long enrollmentId;

    private double marks;
    private String letterGrade;

    public GradeRequest() {}

    public GradeRequest(Long enrollmentId, double marks, String letterGrade) {
        this.enrollmentId = enrollmentId;
        this.marks = marks;
        this.letterGrade = letterGrade;
    }

    public Long getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(Long enrollmentId) { this.enrollmentId = enrollmentId; }
    public double getMarks() { return marks; }
    public void setMarks(double marks) { this.marks = marks; }
    public String getLetterGrade() { return letterGrade; }
    public void setLetterGrade(String letterGrade) { this.letterGrade = letterGrade; }
}
