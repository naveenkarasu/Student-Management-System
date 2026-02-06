package com.studentmgmt.servlet;

import com.studentmgmt.dao.CourseDAO;
import com.studentmgmt.dao.GradeDAO;
import com.studentmgmt.dao.StudentDAO;
import com.studentmgmt.model.Course;
import com.studentmgmt.model.Enrollment;
import com.studentmgmt.model.Faculty;
import com.studentmgmt.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for handling course management operations.
 */
@WebServlet(name = "CourseServlet", urlPatterns = {"/admin/courses", "/admin/course/*"})
public class CourseServlet extends HttpServlet {

    private CourseDAO courseDAO;
    private StudentDAO studentDAO;
    private GradeDAO gradeDAO;

    @Override
    public void init() throws ServletException {
        courseDAO = new CourseDAO();
        studentDAO = new StudentDAO();
        gradeDAO = new GradeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // List all courses
            listCourses(request, response);
        } else if (pathInfo.equals("/new")) {
            // Show add course form
            showAddForm(request, response);
        } else if (pathInfo.equals("/edit")) {
            // Show edit course form
            showEditForm(request, response);
        } else if (pathInfo.equals("/delete")) {
            // Delete course
            deleteCourse(request, response);
        } else if (pathInfo.equals("/view")) {
            // View course details and enrollments
            viewCourse(request, response);
        } else if (pathInfo.equals("/enroll")) {
            // Show enrollment form
            showEnrollForm(request, response);
        } else if (pathInfo.equals("/unenroll")) {
            // Remove enrollment
            unenrollStudent(request, response);
        } else {
            listCourses(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/save")) {
            saveCourse(request, response);
        } else if (pathInfo != null && pathInfo.equals("/update")) {
            updateCourse(request, response);
        } else if (pathInfo != null && pathInfo.equals("/enroll")) {
            enrollStudent(request, response);
        } else {
            listCourses(request, response);
        }
    }

    private void listCourses(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Course> courses = courseDAO.getAllCourses();
        request.setAttribute("courses", courses);
        request.getRequestDispatcher("/admin/courses.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Faculty> facultyList = courseDAO.getAllFaculty();
        request.setAttribute("facultyList", facultyList);
        request.getRequestDispatcher("/admin/course-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int courseId = Integer.parseInt(request.getParameter("id"));
        Course course = courseDAO.getCourseById(courseId);

        if (course != null) {
            List<Faculty> facultyList = courseDAO.getAllFaculty();
            request.setAttribute("course", course);
            request.setAttribute("facultyList", facultyList);
            request.getRequestDispatcher("/admin/course-form.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/courses?error=notfound");
        }
    }

    private void viewCourse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int courseId = Integer.parseInt(request.getParameter("id"));
        Course course = courseDAO.getCourseById(courseId);

        if (course != null) {
            List<Enrollment> enrollments = gradeDAO.getEnrollmentsByCourseId(courseId);
            request.setAttribute("course", course);
            request.setAttribute("enrollments", enrollments);
            request.getRequestDispatcher("/admin/course-view.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/courses?error=notfound");
        }
    }

    private void showEnrollForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int courseId = Integer.parseInt(request.getParameter("id"));
        Course course = courseDAO.getCourseById(courseId);
        List<Student> students = studentDAO.getAllStudents();

        request.setAttribute("course", course);
        request.setAttribute("students", students);
        request.getRequestDispatcher("/admin/enroll-form.jsp").forward(request, response);
    }

    private void saveCourse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get form data
        String courseCode = request.getParameter("courseCode");
        String courseName = request.getParameter("courseName");
        int credits = Integer.parseInt(request.getParameter("credits"));
        String facultyIdStr = request.getParameter("facultyId");
        int facultyId = (facultyIdStr != null && !facultyIdStr.isEmpty()) ? Integer.parseInt(facultyIdStr) : 0;

        // Validate course code uniqueness
        if (courseDAO.courseCodeExists(courseCode)) {
            request.setAttribute("error", "Course code already exists");
            List<Faculty> facultyList = courseDAO.getAllFaculty();
            request.setAttribute("facultyList", facultyList);
            request.getRequestDispatcher("/admin/course-form.jsp").forward(request, response);
            return;
        }

        // Create course object
        Course course = new Course();
        course.setCourseCode(courseCode);
        course.setCourseName(courseName);
        course.setCredits(credits);
        course.setFacultyId(facultyId);

        // Save course
        int courseId = courseDAO.createCourse(course);

        if (courseId > 0) {
            response.sendRedirect(request.getContextPath() + "/admin/courses?success=created");
        } else {
            request.setAttribute("error", "Failed to create course");
            List<Faculty> facultyList = courseDAO.getAllFaculty();
            request.setAttribute("facultyList", facultyList);
            request.getRequestDispatcher("/admin/course-form.jsp").forward(request, response);
        }
    }

    private void updateCourse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get form data
        int courseId = Integer.parseInt(request.getParameter("courseId"));
        String courseCode = request.getParameter("courseCode");
        String courseName = request.getParameter("courseName");
        int credits = Integer.parseInt(request.getParameter("credits"));
        String facultyIdStr = request.getParameter("facultyId");
        int facultyId = (facultyIdStr != null && !facultyIdStr.isEmpty()) ? Integer.parseInt(facultyIdStr) : 0;

        // Get existing course
        Course existingCourse = courseDAO.getCourseById(courseId);

        // Check course code uniqueness (if changed)
        if (!existingCourse.getCourseCode().equals(courseCode) && courseDAO.courseCodeExists(courseCode)) {
            request.setAttribute("error", "Course code already exists");
            request.setAttribute("course", existingCourse);
            List<Faculty> facultyList = courseDAO.getAllFaculty();
            request.setAttribute("facultyList", facultyList);
            request.getRequestDispatcher("/admin/course-form.jsp").forward(request, response);
            return;
        }

        // Update course object
        Course course = new Course();
        course.setCourseId(courseId);
        course.setCourseCode(courseCode);
        course.setCourseName(courseName);
        course.setCredits(credits);
        course.setFacultyId(facultyId);

        // Save course
        boolean success = courseDAO.updateCourse(course);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/courses?success=updated");
        } else {
            request.setAttribute("error", "Failed to update course");
            request.setAttribute("course", course);
            List<Faculty> facultyList = courseDAO.getAllFaculty();
            request.setAttribute("facultyList", facultyList);
            request.getRequestDispatcher("/admin/course-form.jsp").forward(request, response);
        }
    }

    private void deleteCourse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int courseId = Integer.parseInt(request.getParameter("id"));

        boolean success = courseDAO.deleteCourse(courseId);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/courses?success=deleted");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/courses?error=deletefailed");
        }
    }

    private void enrollStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int courseId = Integer.parseInt(request.getParameter("courseId"));
        int studentId = Integer.parseInt(request.getParameter("studentId"));
        String semester = request.getParameter("semester");

        // Check if already enrolled
        if (gradeDAO.isEnrolled(studentId, courseId, semester)) {
            response.sendRedirect(request.getContextPath() + "/admin/course/view?id=" + courseId + "&error=alreadyenrolled");
            return;
        }

        // Create enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);
        enrollment.setSemester(semester);

        int enrollmentId = gradeDAO.createEnrollment(enrollment);

        if (enrollmentId > 0) {
            response.sendRedirect(request.getContextPath() + "/admin/course/view?id=" + courseId + "&success=enrolled");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/course/view?id=" + courseId + "&error=enrollfailed");
        }
    }

    private void unenrollStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int enrollmentId = Integer.parseInt(request.getParameter("enrollmentId"));
        int courseId = Integer.parseInt(request.getParameter("courseId"));

        boolean success = gradeDAO.deleteEnrollment(enrollmentId);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/course/view?id=" + courseId + "&success=unenrolled");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/course/view?id=" + courseId + "&error=unenrollfailed");
        }
    }
}
