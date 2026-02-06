package com.studentmgmt.servlet;

import com.studentmgmt.dao.UserDAO;
import com.studentmgmt.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for handling user management operations (Admin only).
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/admin/users", "/admin/user/*"})
public class UserServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // List all users
            listUsers(request, response);
        } else if (pathInfo.equals("/new")) {
            // Show add user form
            showAddForm(request, response);
        } else if (pathInfo.equals("/edit")) {
            // Show edit user form
            showEditForm(request, response);
        } else if (pathInfo.equals("/delete")) {
            // Delete user
            deleteUser(request, response);
        } else {
            listUsers(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/save")) {
            saveUser(request, response);
        } else if (pathInfo != null && pathInfo.equals("/update")) {
            updateUser(request, response);
        } else {
            listUsers(request, response);
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String roleFilter = request.getParameter("role");
        List<User> users;

        if (roleFilter != null && !roleFilter.isEmpty()) {
            users = userDAO.getUsersByRole(roleFilter);
            request.setAttribute("roleFilter", roleFilter);
        } else {
            users = userDAO.getAllUsers();
        }

        request.setAttribute("users", users);
        request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int userId = Integer.parseInt(request.getParameter("id"));
        User user = userDAO.getUserById(userId);

        if (user != null) {
            request.setAttribute("editUser", user);
            request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/users?error=notfound");
        }
    }

    private void saveUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get form data
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        // Validate username uniqueness
        if (userDAO.usernameExists(username)) {
            request.setAttribute("error", "Username already exists");
            request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
            return;
        }

        // Create user object
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);

        // Save user
        int userId = userDAO.createUser(user);

        if (userId > 0) {
            response.sendRedirect(request.getContextPath() + "/admin/users?success=created");
        } else {
            request.setAttribute("error", "Failed to create user");
            request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
        }
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get form data
        int userId = Integer.parseInt(request.getParameter("userId"));
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        // Get existing user
        User existingUser = userDAO.getUserById(userId);

        // Check username uniqueness (if changed)
        if (!existingUser.getUsername().equals(username) && userDAO.usernameExists(username)) {
            request.setAttribute("error", "Username already exists");
            request.setAttribute("editUser", existingUser);
            request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
            return;
        }

        // Update user object
        User user = new User();
        user.setUserId(userId);
        user.setUsername(username);
        user.setPassword(password.isEmpty() ? existingUser.getPassword() : password);
        user.setRole(role);

        // Save user
        boolean success = userDAO.updateUser(user);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/users?success=updated");
        } else {
            request.setAttribute("error", "Failed to update user");
            request.setAttribute("editUser", user);
            request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int userId = Integer.parseInt(request.getParameter("id"));

        boolean success = userDAO.deleteUser(userId);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/users?success=deleted");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/users?error=deletefailed");
        }
    }
}
