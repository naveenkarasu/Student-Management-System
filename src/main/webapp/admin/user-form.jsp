<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${empty editUser ? 'Add New' : 'Edit'} User - Student Management System</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar navbar-custom navbar-fixed-top">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard">
                    <span class="glyphicon glyphicon-education"></span> SMS
                </a>
            </div>
            <div id="navbar" class="navbar-collapse collapse">
                <ul class="nav navbar-nav">
                    <li><a href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/students">Students</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/courses">Courses</a></li>
                    <li class="active"><a href="${pageContext.request.contextPath}/admin/users">Users</a></li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                            <span class="glyphicon glyphicon-user"></span>
                            ${sessionScope.username}
                            <span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu">
                            <li><a href="${pageContext.request.contextPath}/logout">
                                <span class="glyphicon glyphicon-log-out"></span> Logout
                            </a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Main Content -->
    <div class="container">
        <div class="row">
            <div class="col-md-8 col-md-offset-2">
                <h2>
                    <span class="glyphicon glyphicon-${empty editUser ? 'plus' : 'pencil'}"></span>
                    ${empty editUser ? 'Add New User' : 'Edit User'}
                </h2>
                <hr>

                <!-- Error Message -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                        ${error}
                    </div>
                </c:if>

                <!-- User Form -->
                <div class="form-custom">
                    <form action="${pageContext.request.contextPath}/admin/user/${empty editUser ? 'save' : 'update'}"
                          method="post" class="form-horizontal">

                        <c:if test="${not empty editUser}">
                            <input type="hidden" name="userId" value="${editUser.userId}">
                        </c:if>

                        <div class="form-group">
                            <label for="username" class="col-sm-3 control-label">Username *</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" id="username" name="username"
                                       value="${editUser.username}" required placeholder="Enter username">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="password" class="col-sm-3 control-label">
                                Password ${empty editUser ? '*' : ''}
                            </label>
                            <div class="col-sm-9">
                                <input type="password" class="form-control" id="password" name="password"
                                       placeholder="${empty editUser ? 'Enter password' : 'Leave blank to keep current password'}"
                                       ${empty editUser ? 'required' : ''}>
                                <c:if test="${not empty editUser}">
                                    <span class="help-block">Leave blank to keep current password</span>
                                </c:if>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="role" class="col-sm-3 control-label">Role *</label>
                            <div class="col-sm-9">
                                <select class="form-control" id="role" name="role" required>
                                    <option value="">Select Role</option>
                                    <option value="admin" ${editUser.role == 'admin' ? 'selected' : ''}>Admin</option>
                                    <option value="faculty" ${editUser.role == 'faculty' ? 'selected' : ''}>Faculty</option>
                                    <option value="student" ${editUser.role == 'student' ? 'selected' : ''}>Student</option>
                                </select>
                            </div>
                        </div>

                        <hr>

                        <div class="form-group">
                            <div class="col-sm-offset-3 col-sm-9">
                                <button type="submit" class="btn btn-primary">
                                    <span class="glyphicon glyphicon-save"></span>
                                    ${empty editUser ? 'Save User' : 'Update User'}
                                </button>
                                <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-default">
                                    <span class="glyphicon glyphicon-arrow-left"></span> Cancel
                                </a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <div class="footer">
        <div class="container">
            <p>Student Management System &copy; 2018</p>
        </div>
    </div>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>
