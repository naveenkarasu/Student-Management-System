package com.studentmgmt.dao;

import com.studentmgmt.model.Enrollment;
import com.studentmgmt.model.Grade;
import com.studentmgmt.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Grade and Enrollment operations.
 */
public class GradeDAO {

    // ============== Enrollment Methods ==============

    /**
     * Get all enrollments for a student.
     *
     * @param studentId the student ID
     * @return List of enrollments
     */
    public List<Enrollment> getEnrollmentsByStudentId(int studentId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT e.*, s.name as student_name, s.roll_number, " +
                     "c.course_code, c.course_name, c.credits " +
                     "FROM enrollments e " +
                     "JOIN students s ON e.student_id = s.student_id " +
                     "JOIN courses c ON e.course_id = c.course_id " +
                     "WHERE e.student_id = ? ORDER BY e.semester, c.course_code";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                enrollments.add(extractEnrollmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return enrollments;
    }

    /**
     * Get all enrollments for a course.
     *
     * @param courseId the course ID
     * @return List of enrollments
     */
    public List<Enrollment> getEnrollmentsByCourseId(int courseId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT e.*, s.name as student_name, s.roll_number, " +
                     "c.course_code, c.course_name, c.credits " +
                     "FROM enrollments e " +
                     "JOIN students s ON e.student_id = s.student_id " +
                     "JOIN courses c ON e.course_id = c.course_id " +
                     "WHERE e.course_id = ? ORDER BY s.roll_number";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, courseId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                enrollments.add(extractEnrollmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return enrollments;
    }

    /**
     * Get enrollment by ID.
     *
     * @param enrollmentId the enrollment ID
     * @return Enrollment object if found, null otherwise
     */
    public Enrollment getEnrollmentById(int enrollmentId) {
        String sql = "SELECT e.*, s.name as student_name, s.roll_number, " +
                     "c.course_code, c.course_name, c.credits " +
                     "FROM enrollments e " +
                     "JOIN students s ON e.student_id = s.student_id " +
                     "JOIN courses c ON e.course_id = c.course_id " +
                     "WHERE e.enrollment_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, enrollmentId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return extractEnrollmentFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return null;
    }

    /**
     * Create a new enrollment.
     *
     * @param enrollment the enrollment to create
     * @return the generated enrollment ID, or -1 if failed
     */
    public int createEnrollment(Enrollment enrollment) {
        String sql = "INSERT INTO enrollments (student_id, course_id, semester) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, enrollment.getStudentId());
            stmt.setInt(2, enrollment.getCourseId());
            stmt.setString(3, enrollment.getSemester());

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
     * Delete an enrollment.
     *
     * @param enrollmentId the enrollment ID
     * @return true if successful, false otherwise
     */
    public boolean deleteEnrollment(int enrollmentId) {
        String sql = "DELETE FROM enrollments WHERE enrollment_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, enrollmentId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, null);
        }
        return false;
    }

    /**
     * Check if student is already enrolled in a course for a semester.
     *
     * @param studentId the student ID
     * @param courseId the course ID
     * @param semester the semester
     * @return true if already enrolled, false otherwise
     */
    public boolean isEnrolled(int studentId, int courseId, String semester) {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE student_id = ? AND course_id = ? AND semester = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            stmt.setString(3, semester);
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

    // ============== Grade Methods ==============

    /**
     * Get all grades for a student.
     *
     * @param studentId the student ID
     * @return List of grades
     */
    public List<Grade> getGradesByStudentId(int studentId) {
        List<Grade> grades = new ArrayList<>();
        String sql = "SELECT g.*, e.semester, s.name as student_name, s.roll_number, " +
                     "c.course_code, c.course_name, c.credits " +
                     "FROM grades g " +
                     "JOIN enrollments e ON g.enrollment_id = e.enrollment_id " +
                     "JOIN students s ON e.student_id = s.student_id " +
                     "JOIN courses c ON e.course_id = c.course_id " +
                     "WHERE e.student_id = ? ORDER BY e.semester, c.course_code";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                grades.add(extractGradeFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return grades;
    }

    /**
     * Get grade by enrollment ID.
     *
     * @param enrollmentId the enrollment ID
     * @return Grade object if found, null otherwise
     */
    public Grade getGradeByEnrollmentId(int enrollmentId) {
        String sql = "SELECT g.*, e.semester, s.name as student_name, s.roll_number, " +
                     "c.course_code, c.course_name, c.credits " +
                     "FROM grades g " +
                     "JOIN enrollments e ON g.enrollment_id = e.enrollment_id " +
                     "JOIN students s ON e.student_id = s.student_id " +
                     "JOIN courses c ON e.course_id = c.course_id " +
                     "WHERE g.enrollment_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, enrollmentId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return extractGradeFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return null;
    }

    /**
     * Get grades for a course (for faculty grading).
     *
     * @param courseId the course ID
     * @return List of enrollments with grades (or null grades if not graded)
     */
    public List<Grade> getGradesForCourse(int courseId) {
        List<Grade> grades = new ArrayList<>();
        String sql = "SELECT e.enrollment_id, e.semester, s.name as student_name, s.roll_number, " +
                     "c.course_code, c.course_name, c.credits, " +
                     "g.grade_id, g.marks, g.grade, g.graded_at " +
                     "FROM enrollments e " +
                     "JOIN students s ON e.student_id = s.student_id " +
                     "JOIN courses c ON e.course_id = c.course_id " +
                     "LEFT JOIN grades g ON e.enrollment_id = g.enrollment_id " +
                     "WHERE e.course_id = ? ORDER BY s.roll_number";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, courseId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Grade grade = new Grade();
                grade.setEnrollmentId(rs.getInt("enrollment_id"));
                grade.setGradeId(rs.getInt("grade_id"));
                grade.setMarks(rs.getDouble("marks"));
                grade.setGrade(rs.getString("grade"));
                grade.setGradedAt(rs.getTimestamp("graded_at"));
                grade.setSemester(rs.getString("semester"));
                grade.setStudentName(rs.getString("student_name"));
                grade.setStudentRollNumber(rs.getString("roll_number"));
                grade.setCourseCode(rs.getString("course_code"));
                grade.setCourseName(rs.getString("course_name"));
                grade.setCredits(rs.getInt("credits"));
                grades.add(grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return grades;
    }

    /**
     * Add or update a grade.
     *
     * @param grade the grade to save
     * @return true if successful, false otherwise
     */
    public boolean saveGrade(Grade grade) {
        // Check if grade exists
        Grade existing = getGradeByEnrollmentId(grade.getEnrollmentId());

        if (existing != null) {
            // Update existing grade
            String sql = "UPDATE grades SET marks = ?, grade = ? WHERE enrollment_id = ?";
            Connection conn = null;
            PreparedStatement stmt = null;

            try {
                conn = DBConnection.getConnection();
                stmt = conn.prepareStatement(sql);
                stmt.setDouble(1, grade.getMarks());
                stmt.setString(2, grade.getGrade());
                stmt.setInt(3, grade.getEnrollmentId());

                return stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeResources(conn, stmt, null);
            }
        } else {
            // Insert new grade
            String sql = "INSERT INTO grades (enrollment_id, marks, grade) VALUES (?, ?, ?)";
            Connection conn = null;
            PreparedStatement stmt = null;

            try {
                conn = DBConnection.getConnection();
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, grade.getEnrollmentId());
                stmt.setDouble(2, grade.getMarks());
                stmt.setString(3, grade.getGrade());

                return stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeResources(conn, stmt, null);
            }
        }
        return false;
    }

    /**
     * Delete a grade.
     *
     * @param gradeId the grade ID
     * @return true if successful, false otherwise
     */
    public boolean deleteGrade(int gradeId) {
        String sql = "DELETE FROM grades WHERE grade_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, gradeId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, null);
        }
        return false;
    }

    /**
     * Calculate letter grade from marks.
     *
     * @param marks the marks
     * @return letter grade
     */
    public static String calculateGrade(double marks) {
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

    /**
     * Get enrollment count.
     *
     * @return total number of enrollments
     */
    public int getEnrollmentCount() {
        String sql = "SELECT COUNT(*) FROM enrollments";
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

    private Enrollment extractEnrollmentFromResultSet(ResultSet rs) throws SQLException {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(rs.getInt("enrollment_id"));
        enrollment.setStudentId(rs.getInt("student_id"));
        enrollment.setCourseId(rs.getInt("course_id"));
        enrollment.setSemester(rs.getString("semester"));
        enrollment.setStudentName(rs.getString("student_name"));
        enrollment.setStudentRollNumber(rs.getString("roll_number"));
        enrollment.setCourseCode(rs.getString("course_code"));
        enrollment.setCourseName(rs.getString("course_name"));
        enrollment.setCredits(rs.getInt("credits"));
        return enrollment;
    }

    private Grade extractGradeFromResultSet(ResultSet rs) throws SQLException {
        Grade grade = new Grade();
        grade.setGradeId(rs.getInt("grade_id"));
        grade.setEnrollmentId(rs.getInt("enrollment_id"));
        grade.setMarks(rs.getDouble("marks"));
        grade.setGrade(rs.getString("grade"));
        grade.setGradedAt(rs.getTimestamp("graded_at"));
        grade.setSemester(rs.getString("semester"));
        grade.setStudentName(rs.getString("student_name"));
        grade.setStudentRollNumber(rs.getString("roll_number"));
        grade.setCourseCode(rs.getString("course_code"));
        grade.setCourseName(rs.getString("course_name"));
        grade.setCredits(rs.getInt("credits"));
        return grade;
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
