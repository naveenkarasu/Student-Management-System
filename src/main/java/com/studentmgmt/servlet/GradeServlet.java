package com.studentmgmt.servlet;

import com.studentmgmt.dao.CourseDAO;
import com.studentmgmt.dao.GradeDAO;
import com.studentmgmt.dao.StudentDAO;
import com.studentmgmt.model.*;
import com.studentmgmt.util.PDFGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Servlet for handling grade management operations.
 * Used by faculty to enter grades and by students to view grades.
 */
@WebServlet(name = "GradeServlet", urlPatterns = {
    "/faculty/grades", "/faculty/grade/*",
    "/student/grades", "/student/transcript"
})
public class GradeServlet extends HttpServlet {

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

        String servletPath = request.getServletPath();

        if (servletPath.startsWith("/faculty")) {
            handleFacultyGet(request, response);
        } else if (servletPath.startsWith("/student")) {
            handleStudentGet(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();

        if (servletPath.startsWith("/faculty")) {
            handleFacultyPost(request, response);
        }
    }

    // ============== Faculty Methods ==============

    private void handleFacultyGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Get faculty info
        Faculty faculty = courseDAO.getFacultyByUserId(user.getUserId());

        if (faculty == null) {
            request.setAttribute("error", "Faculty profile not found");
            request.getRequestDispatcher("/faculty/grades.jsp").forward(request, response);
            return;
        }

        if (pathInfo == null || pathInfo.equals("/")) {
            // List courses taught by faculty
            List<Course> courses = courseDAO.getCoursesByFacultyId(faculty.getFacultyId());
            request.setAttribute("courses", courses);
            request.setAttribute("faculty", faculty);
            request.getRequestDispatcher("/faculty/grades.jsp").forward(request, response);
        } else if (pathInfo.equals("/course")) {
            // Show grades for a specific course
            int courseId = Integer.parseInt(request.getParameter("id"));
            Course course = courseDAO.getCourseById(courseId);

            // Verify faculty owns this course
            if (course != null && course.getFacultyId() == faculty.getFacultyId()) {
                List<Grade> grades = gradeDAO.getGradesForCourse(courseId);
                request.setAttribute("course", course);
                request.setAttribute("grades", grades);
                request.getRequestDispatcher("/faculty/grade-entry.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/faculty/grades?error=unauthorized");
            }
        }
    }

    private void handleFacultyPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/save")) {
            saveGrades(request, response);
        }
    }

    private void saveGrades(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int courseId = Integer.parseInt(request.getParameter("courseId"));
        String[] enrollmentIds = request.getParameterValues("enrollmentId");
        String[] marksArray = request.getParameterValues("marks");

        if (enrollmentIds != null && marksArray != null) {
            for (int i = 0; i < enrollmentIds.length; i++) {
                int enrollmentId = Integer.parseInt(enrollmentIds[i]);
                String marksStr = marksArray[i];

                if (marksStr != null && !marksStr.trim().isEmpty()) {
                    double marks = Double.parseDouble(marksStr.trim());
                    String letterGrade = GradeDAO.calculateGrade(marks);

                    Grade grade = new Grade();
                    grade.setEnrollmentId(enrollmentId);
                    grade.setMarks(marks);
                    grade.setGrade(letterGrade);

                    gradeDAO.saveGrade(grade);
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/faculty/grade/course?id=" + courseId + "&success=saved");
    }

    // ============== Student Methods ==============

    private void handleStudentGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Get student info
        Student student = studentDAO.getStudentByUserId(user.getUserId());

        if (student == null) {
            request.setAttribute("error", "Student profile not found");
            request.getRequestDispatcher("/student/grades.jsp").forward(request, response);
            return;
        }

        if (servletPath.equals("/student/grades")) {
            // Show student grades
            List<Grade> grades = gradeDAO.getGradesByStudentId(student.getStudentId());
            request.setAttribute("student", student);
            request.setAttribute("grades", grades);

            // Calculate GPA
            double totalCredits = 0;
            double totalGradePoints = 0;
            for (Grade grade : grades) {
                totalCredits += grade.getCredits();
                totalGradePoints += (grade.getCredits() * grade.getGradePoints());
            }
            double gpa = totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;
            request.setAttribute("gpa", String.format("%.2f", gpa));
            request.setAttribute("totalCredits", (int) totalCredits);

            request.getRequestDispatcher("/student/grades.jsp").forward(request, response);
        } else if (servletPath.equals("/student/transcript")) {
            // Generate PDF transcript
            generateTranscript(request, response, student);
        }
    }

    private void generateTranscript(HttpServletRequest request, HttpServletResponse response, Student student)
            throws ServletException, IOException {

        List<Grade> grades = gradeDAO.getGradesByStudentId(student.getStudentId());

        // Set response headers for PDF download
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"transcript_" +
                student.getRollNumber() + ".pdf\"");

        try {
            OutputStream outputStream = response.getOutputStream();
            PDFGenerator.generateTranscript(student, grades, outputStream);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/student/grades?error=pdffailed");
        }
    }
}
