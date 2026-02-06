package com.studentmgmt.model;

import java.sql.Timestamp;

/**
 * Enrollment model class representing a student's enrollment in a course.
 */
public class Enrollment {
    private int enrollmentId;
    private int studentId;
    private int courseId;
    private String semester;
    private Timestamp enrolledAt;

    // For display purposes
    private String studentName;
    private String studentRollNumber;
    private String courseCode;
    private String courseName;
    private int credits;

    public Enrollment() {
    }

    public Enrollment(int enrollmentId, int studentId, int courseId, String semester) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.semester = semester;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Timestamp getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(Timestamp enrolledAt) {
        this.enrolledAt = enrolledAt;
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

    @Override
    public String toString() {
        return "Enrollment{" +
                "enrollmentId=" + enrollmentId +
                ", studentId=" + studentId +
                ", courseId=" + courseId +
                ", semester='" + semester + '\'' +
                '}';
    }
}
