package com.studentmgmt.dao;

import com.studentmgmt.model.Course;
import com.studentmgmt.model.Faculty;
import com.studentmgmt.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Course operations.
 */
public class CourseDAO {

    /**
     * Get all courses.
     *
     * @return List of all courses
     */
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.*, f.name as faculty_name FROM courses c " +
                     "LEFT JOIN faculty f ON c.faculty_id = f.faculty_id " +
                     "ORDER BY c.course_code";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                courses.add(extractCourseFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return courses;
    }

    /**
     * Get course by ID.
     *
     * @param courseId the course ID
     * @return Course object if found, null otherwise
     */
    public Course getCourseById(int courseId) {
        String sql = "SELECT c.*, f.name as faculty_name FROM courses c " +
                     "LEFT JOIN faculty f ON c.faculty_id = f.faculty_id " +
                     "WHERE c.course_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, courseId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return extractCourseFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return null;
    }

    /**
     * Get course by course code.
     *
     * @param courseCode the course code
     * @return Course object if found, null otherwise
     */
    public Course getCourseByCode(String courseCode) {
        String sql = "SELECT c.*, f.name as faculty_name FROM courses c " +
                     "LEFT JOIN faculty f ON c.faculty_id = f.faculty_id " +
                     "WHERE c.course_code = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, courseCode);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return extractCourseFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return null;
    }

    /**
     * Get courses by faculty ID.
     *
     * @param facultyId the faculty ID
     * @return List of courses taught by the faculty
     */
    public List<Course> getCoursesByFacultyId(int facultyId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.*, f.name as faculty_name FROM courses c " +
                     "LEFT JOIN faculty f ON c.faculty_id = f.faculty_id " +
                     "WHERE c.faculty_id = ? ORDER BY c.course_code";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, facultyId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                courses.add(extractCourseFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return courses;
    }

    /**
     * Create a new course.
     *
     * @param course the course to create
     * @return the generated course ID, or -1 if failed
     */
    public int createCourse(Course course) {
        String sql = "INSERT INTO courses (course_code, course_name, credits, faculty_id) " +
                     "VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, course.getCourseCode());
            stmt.setString(2, course.getCourseName());
            stmt.setInt(3, course.getCredits());

            if (course.getFacultyId() > 0) {
                stmt.setInt(4, course.getFacultyId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

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
     * Update an existing course.
     *
     * @param course the course to update
     * @return true if successful, false otherwise
     */
    public boolean updateCourse(Course course) {
        String sql = "UPDATE courses SET course_code = ?, course_name = ?, credits = ?, " +
                     "faculty_id = ? WHERE course_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, course.getCourseCode());
            stmt.setString(2, course.getCourseName());
            stmt.setInt(3, course.getCredits());

            if (course.getFacultyId() > 0) {
                stmt.setInt(4, course.getFacultyId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setInt(5, course.getCourseId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, null);
        }
        return false;
    }

    /**
     * Delete a course by ID.
     *
     * @param courseId the course ID
     * @return true if successful, false otherwise
     */
    public boolean deleteCourse(int courseId) {
        String sql = "DELETE FROM courses WHERE course_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, courseId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, null);
        }
        return false;
    }

    /**
     * Check if course code exists.
     *
     * @param courseCode the course code to check
     * @return true if exists, false otherwise
     */
    public boolean courseCodeExists(String courseCode) {
        String sql = "SELECT COUNT(*) FROM courses WHERE course_code = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, courseCode);
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
     * Get all faculty members.
     *
     * @return List of all faculty
     */
    public List<Faculty> getAllFaculty() {
        List<Faculty> facultyList = new ArrayList<>();
        String sql = "SELECT * FROM faculty ORDER BY name";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Faculty faculty = new Faculty();
                faculty.setFacultyId(rs.getInt("faculty_id"));
                faculty.setUserId(rs.getInt("user_id"));
                faculty.setName(rs.getString("name"));
                faculty.setEmail(rs.getString("email"));
                faculty.setDepartment(rs.getString("department"));
                facultyList.add(faculty);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return facultyList;
    }

    /**
     * Get faculty by user ID.
     *
     * @param userId the user ID
     * @return Faculty object if found, null otherwise
     */
    public Faculty getFacultyByUserId(int userId) {
        String sql = "SELECT * FROM faculty WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Faculty faculty = new Faculty();
                faculty.setFacultyId(rs.getInt("faculty_id"));
                faculty.setUserId(rs.getInt("user_id"));
                faculty.setName(rs.getString("name"));
                faculty.setEmail(rs.getString("email"));
                faculty.setDepartment(rs.getString("department"));
                return faculty;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return null;
    }

    /**
     * Get total count of courses.
     *
     * @return total number of courses
     */
    public int getCourseCount() {
        String sql = "SELECT COUNT(*) FROM courses";
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

    private Course extractCourseFromResultSet(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setCourseId(rs.getInt("course_id"));
        course.setCourseCode(rs.getString("course_code"));
        course.setCourseName(rs.getString("course_name"));
        course.setCredits(rs.getInt("credits"));
        course.setFacultyId(rs.getInt("faculty_id"));
        try {
            course.setFacultyName(rs.getString("faculty_name"));
        } catch (SQLException e) {
            // faculty_name not in result set
        }
        return course;
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
