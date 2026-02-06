<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login - Student Management System</title>

    <!-- Bootstrap 3 CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <div class="login-container">
            <h2><span class="glyphicon glyphicon-education"></span> Student Management System</h2>

            <!-- Error Message -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible" role="alert">
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    ${error}
                </div>
            </c:if>

            <!-- Success Message (Logout) -->
            <c:if test="${param.message == 'logged_out'}">
                <div class="alert alert-success alert-dismissible" role="alert">
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    You have been successfully logged out.
                </div>
            </c:if>

            <!-- Login Form -->
            <form action="${pageContext.request.contextPath}/login" method="post">
                <div class="form-group">
                    <label for="username">
                        <span class="glyphicon glyphicon-user"></span> Username
                    </label>
                    <input type="text" class="form-control" id="username" name="username"
                           placeholder="Enter username" value="${username}" required>
                </div>

                <div class="form-group">
                    <label for="password">
                        <span class="glyphicon glyphicon-lock"></span> Password
                    </label>
                    <input type="password" class="form-control" id="password" name="password"
                           placeholder="Enter password" required>
                </div>

                <button type="submit" class="btn btn-primary">
                    <span class="glyphicon glyphicon-log-in"></span> Login
                </button>
            </form>

            <hr>

            <div class="text-center">
                <p class="text-muted">
                    <small>Student Management System &copy; 2018</small>
                </p>
            </div>

            <!-- Demo Credentials -->
            <div class="panel panel-info">
                <div class="panel-heading">
                    <h4 class="panel-title">Demo Credentials</h4>
                </div>
                <div class="panel-body">
                    <p><strong>Admin:</strong> admin / admin123</p>
                    <p><strong>Faculty:</strong> faculty1 / faculty123</p>
                    <p><strong>Student:</strong> student1 / student123</p>
                </div>
            </div>
        </div>
    </div>

    <!-- jQuery and Bootstrap JS -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>
