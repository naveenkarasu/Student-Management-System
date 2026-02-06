package com.studentmgmt.servlet;

import com.studentmgmt.dao.UserDAO;
import com.studentmgmt.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet handling user login and logout operations.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login", "/logout"})
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getServletPath();

        if ("/logout".equals(action)) {
            // Handle logout
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp?message=logged_out");
        } else {
            // Redirect to login page
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Validate input
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Username and password are required");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Authenticate user
        User user = userDAO.authenticate(username.trim(), password);

        if (user != null) {
            // Create session and store user
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());

            // Set session timeout (30 minutes)
            session.setMaxInactiveInterval(30 * 60);

            // Redirect based on role
            String redirectUrl = getRedirectUrl(user.getRole(), request.getContextPath());
            response.sendRedirect(redirectUrl);
        } else {
            // Authentication failed
            request.setAttribute("error", "Invalid username or password");
            request.setAttribute("username", username);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    /**
     * Get redirect URL based on user role.
     */
    private String getRedirectUrl(String role, String contextPath) {
        switch (role) {
            case "admin":
                return contextPath + "/dashboard";
            case "faculty":
                return contextPath + "/dashboard";
            case "student":
                return contextPath + "/dashboard";
            default:
                return contextPath + "/login.jsp";
        }
    }
}
