package com.studentmgmt.model;

/**
 * Faculty model class representing a faculty member.
 */
public class Faculty {
    private int facultyId;
    private int userId;
    private String name;
    private String email;
    private String department;

    public Faculty() {
    }

    public Faculty(int facultyId, int userId, String name, String email, String department) {
        this.facultyId = facultyId;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.department = department;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    @Override
    public String toString() {
        return "Faculty{" +
                "facultyId=" + facultyId +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
