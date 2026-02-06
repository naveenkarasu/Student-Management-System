package com.studentmgmt.model;

/**
 * Student model class representing a student entity.
 */
public class Student {
    private int studentId;
    private int userId;
    private String rollNumber;
    private String name;
    private String email;
    private String department;
    private int semester;

    public Student() {
    }

    public Student(int studentId, String rollNumber, String name, String email, String department, int semester) {
        this.studentId = studentId;
        this.rollNumber = rollNumber;
        this.name = name;
        this.email = email;
        this.department = department;
        this.semester = semester;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", rollNumber='" + rollNumber + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", semester=" + semester +
                '}';
    }
}
