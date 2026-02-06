package com.studentmgmt.servlet;

import com.studentmgmt.dao.StudentDAO;
import com.studentmgmt.dao.UserDAO;
import com.studentmgmt.model.Student;
import com.studentmgmt.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for handling student CRUD operations.
 */
@WebServlet(name = "StudentServlet", urlPatterns = {"/admin/students", "/admin/student/*"})
public class StudentServlet extends HttpServlet {

    private StudentDAO studentDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        studentDAO = new StudentDAO();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        String action = request.getParameter("action");

        if (pathInfo == null || pathInfo.equals("/")) {
            // List all students
            listStudents(request, response);
        } else if (pathInfo.equals("/new")) {
            // Show add student form
            showAddForm(request, response);
        } else if (pathInfo.equals("/edit")) {
            // Show edit student form
            showEditForm(request, response);
        } else if (pathInfo.equals("/delete")) {
            // Delete student
            deleteStudent(request, response);
        } else if (pathInfo.equals("/view")) {
            // View student details
            viewStudent(request, response);
        } else {
            listStudents(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/save")) {
            saveStudent(request, response);
        } else if (pathInfo != null && pathInfo.equals("/update")) {
            updateStudent(request, response);
        } else {
            listStudents(request, response);
        }
    }

    private void listStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String search = request.getParameter("search");
        List<Student> students;

        if (search != null && !search.trim().isEmpty()) {
            students = studentDAO.searchStudents(search.trim());
            request.setAttribute("search", search);
        } else {
            students = studentDAO.getAllStudents();
        }

        request.setAttribute("students", students);
        request.getRequestDispatcher("/admin/students.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/admin/student-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int studentId = Integer.parseInt(request.getParameter("id"));
        Student student = studentDAO.getStudentById(studentId);

        if (student != null) {
            request.setAttribute("student", student);
            request.getRequestDispatcher("/admin/student-form.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/students?error=notfound");
        }
    }

    private void viewStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int studentId = Integer.parseInt(request.getParameter("id"));
        Student student = studentDAO.getStudentById(studentId);

        if (student != null) {
            request.setAttribute("student", student);
            request.getRequestDispatcher("/admin/student-view.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/students?error=notfound");
        }
    }

    private void saveStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get form data
        String rollNumber = request.getParameter("rollNumber");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String department = request.getParameter("department");
        int semester = Integer.parseInt(request.getParameter("semester"));
        String createUser = request.getParameter("createUser");

        // Validate roll number uniqueness
        if (studentDAO.rollNumberExists(rollNumber)) {
            request.setAttribute("error", "Roll number already exists");
            request.getRequestDispatcher("/admin/student-form.jsp").forward(request, response);
            return;
        }

        // Create student object
        Student student = new Student();
        student.setRollNumber(rollNumber);
        student.setName(name);
        student.setEmail(email);
        student.setDepartment(department);
        student.setSemester(semester);

        // Create user account if requested
        if ("yes".equals(createUser)) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            if (userDAO.usernameExists(username)) {
                request.setAttribute("error", "Username already exists");
                request.getRequestDispatcher("/admin/student-form.jsp").forward(request, response);
                return;
            }

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setRole("student");

            int userId = userDAO.createUser(user);
            if (userId > 0) {
                student.setUserId(userId);
            }
        }

        // Save student
        int studentId = studentDAO.createStudent(student);

        if (studentId > 0) {
            response.sendRedirect(request.getContextPath() + "/admin/students?success=created");
        } else {
            request.setAttribute("error", "Failed to create student");
            request.getRequestDispatcher("/admin/student-form.jsp").forward(request, response);
        }
    }

    private void updateStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get form data
        int studentId = Integer.parseInt(request.getParameter("studentId"));
        String rollNumber = request.getParameter("rollNumber");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String department = request.getParameter("department");
        int semester = Integer.parseInt(request.getParameter("semester"));

        // Get existing student
        Student existingStudent = studentDAO.getStudentById(studentId);

        // Check roll number uniqueness (if changed)
        if (!existingStudent.getRollNumber().equals(rollNumber) && studentDAO.rollNumberExists(rollNumber)) {
            request.setAttribute("error", "Roll number already exists");
            request.setAttribute("student", existingStudent);
            request.getRequestDispatcher("/admin/student-form.jsp").forward(request, response);
            return;
        }

        // Update student object
        Student student = new Student();
        student.setStudentId(studentId);
        student.setRollNumber(rollNumber);
        student.setName(name);
        student.setEmail(email);
        student.setDepartment(department);
        student.setSemester(semester);

        // Save student
        boolean success = studentDAO.updateStudent(student);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/students?success=updated");
        } else {
            request.setAttribute("error", "Failed to update student");
            request.setAttribute("student", student);
            request.getRequestDispatcher("/admin/student-form.jsp").forward(request, response);
        }
    }

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int studentId = Integer.parseInt(request.getParameter("id"));

        boolean success = studentDAO.deleteStudent(studentId);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/students?success=deleted");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/students?error=deletefailed");
        }
    }
}
