package com.studentmgmt.dto;

import java.util.List;
import java.util.Map;

public class TranscriptData {
    private String studentName;
    private String rollNumber;
    private String department;
    private int semester;
    private List<GradeResponse> grades;
    private Map<String, List<GradeResponse>> gradesBySemester;
    private double cumulativeGpa;
    private int totalCredits;

    public TranscriptData() {}

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }
    public List<GradeResponse> getGrades() { return grades; }
    public void setGrades(List<GradeResponse> grades) { this.grades = grades; }
    public Map<String, List<GradeResponse>> getGradesBySemester() { return gradesBySemester; }
    public void setGradesBySemester(Map<String, List<GradeResponse>> gradesBySemester) { this.gradesBySemester = gradesBySemester; }
    public double getCumulativeGpa() { return cumulativeGpa; }
    public void setCumulativeGpa(double cumulativeGpa) { this.cumulativeGpa = cumulativeGpa; }
    public int getTotalCredits() { return totalCredits; }
    public void setTotalCredits(int totalCredits) { this.totalCredits = totalCredits; }
}
