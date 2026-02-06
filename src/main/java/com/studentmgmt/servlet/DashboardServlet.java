package com.studentmgmt.servlet;

import com.studentmgmt.dao.CourseDAO;
import com.studentmgmt.dao.GradeDAO;
import com.studentmgmt.dao.StudentDAO;
import com.studentmgmt.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet for handling dashboard display based on user role.
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {

    private StudentDAO studentDAO;
    private CourseDAO courseDAO;
    private GradeDAO gradeDAO;

    @Override
    public void init() throws ServletException {
        studentDAO = new StudentDAO();
        courseDAO = new CourseDAO();
        gradeDAO = new GradeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        String role = user.getRole();

        // Add dashboard statistics
        request.setAttribute("studentCount", studentDAO.getStudentCount());
        request.setAttribute("courseCount", courseDAO.getCourseCount());
        request.setAttribute("enrollmentCount", gradeDAO.getEnrollmentCount());

        // Forward to dashboard.jsp
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
