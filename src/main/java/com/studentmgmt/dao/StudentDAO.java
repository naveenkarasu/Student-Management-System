package com.studentmgmt.dao;

import com.studentmgmt.model.Student;
import com.studentmgmt.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Student operations.
 */
public class StudentDAO {

    /**
     * Get all students.
     *
     * @return List of all students
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY name";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return students;
    }

    /**
     * Get student by ID.
     *
     * @param studentId the student ID
     * @return Student object if found, null otherwise
     */
    public Student getStudentById(int studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return extractStudentFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return null;
    }

    /**
     * Get student by user ID.
     *
     * @param userId the user ID
     * @return Student object if found, null otherwise
     */
    public Student getStudentByUserId(int userId) {
        String sql = "SELECT * FROM students WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return extractStudentFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return null;
    }

    /**
     * Get student by roll number.
     *
     * @param rollNumber the roll number
     * @return Student object if found, null otherwise
     */
    public Student getStudentByRollNumber(String rollNumber) {
        String sql = "SELECT * FROM students WHERE roll_number = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, rollNumber);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return extractStudentFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return null;
    }

    /**
     * Get students by department.
     *
     * @param department the department name
     * @return List of students in the department
     */
    public List<Student> getStudentsByDepartment(String department) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE department = ? ORDER BY name";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, department);
            rs = stmt.executeQuery();

            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return students;
    }

    /**
     * Create a new student.
     *
     * @param student the student to create
     * @return the generated student ID, or -1 if failed
     */
    public int createStudent(Student student) {
        String sql = "INSERT INTO students (user_id, roll_number, name, email, department, semester) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            if (student.getUserId() > 0) {
                stmt.setInt(1, student.getUserId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, student.getRollNumber());
            stmt.setString(3, student.getName());
            stmt.setString(4, student.getEmail());
            stmt.setString(5, student.getDepartment());
            stmt.setInt(6, student.getSemester());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return -1;
    }

    /**
     * Update an existing student.
     *
     * @param student the student to update
     * @return true if successful, false otherwise
     */
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET roll_number = ?, name = ?, email = ?, " +
                     "department = ?, semester = ? WHERE student_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, student.getRollNumber());
            stmt.setString(2, student.getName());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getDepartment());
            stmt.setInt(5, student.getSemester());
            stmt.setInt(6, student.getStudentId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, null);
        }
        return false;
    }

    /**
     * Delete a student by ID.
     *
     * @param studentId the student ID
     * @return true if successful, false otherwise
     */
    public boolean deleteStudent(int studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, null);
        }
        return false;
    }

    /**
     * Check if roll number exists.
     *
     * @param rollNumber the roll number to check
     * @return true if exists, false otherwise
     */
    public boolean rollNumberExists(String rollNumber) {
        String sql = "SELECT COUNT(*) FROM students WHERE roll_number = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, rollNumber);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return false;
    }

    /**
     * Search students by name or roll number.
     *
     * @param searchTerm the search term
     * @return List of matching students
     */
    public List<Student> searchStudents(String searchTerm) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE name LIKE ? OR roll_number LIKE ? ORDER BY name";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            String pattern = "%" + searchTerm + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            rs = stmt.executeQuery();

            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return students;
    }

    /**
     * Get total count of students.
     *
     * @return total number of students
     */
    public int getStudentCount() {
        String sql = "SELECT COUNT(*) FROM students";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return 0;
    }

    private Student extractStudentFromResultSet(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setStudentId(rs.getInt("student_id"));
        student.setUserId(rs.getInt("user_id"));
        student.setRollNumber(rs.getString("roll_number"));
        student.setName(rs.getString("name"));
        student.setEmail(rs.getString("email"));
        student.setDepartment(rs.getString("department"));
        student.setSemester(rs.getInt("semester"));
        return student;
    }

    private void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
