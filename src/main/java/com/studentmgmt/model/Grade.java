package com.studentmgmt.model;

import java.sql.Timestamp;

/**
 * Grade model class representing a grade for an enrollment.
 */
public class Grade {
    private int gradeId;
    private int enrollmentId;
    private double marks;
    private String grade;
    private Timestamp gradedAt;

    // For display purposes
    private String courseCode;
    private String courseName;
    private int credits;
    private String semester;
    private String studentName;
    private String studentRollNumber;

    public Grade() {
    }

    public Grade(int gradeId, int enrollmentId, double marks, String grade) {
        this.gradeId = gradeId;
        this.enrollmentId = enrollmentId;
        this.marks = marks;
        this.grade = grade;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Timestamp getGradedAt() {
        return gradedAt;
    }

    public void setGradedAt(Timestamp gradedAt) {
        this.gradedAt = gradedAt;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentRollNumber() {
        return studentRollNumber;
    }

    public void setStudentRollNumber(String studentRollNumber) {
        this.studentRollNumber = studentRollNumber;
    }

    /**
     * Calculate grade points based on letter grade.
     * @return grade points (4.0 scale)
     */
    public double getGradePoints() {
        if (grade == null) return 0.0;
        switch (grade.toUpperCase()) {
            case "A+": return 4.0;
            case "A": return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B": return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C": return 2.0;
            case "C-": return 1.7;
            case "D+": return 1.3;
            case "D": return 1.0;
            case "F": return 0.0;
            default: return 0.0;
        }
    }

    @Override
    public String toString() {
        return "Grade{" +
                "gradeId=" + gradeId +
                ", enrollmentId=" + enrollmentId +
                ", marks=" + marks +
                ", grade='" + grade + '\'' +
                '}';
    }
}
