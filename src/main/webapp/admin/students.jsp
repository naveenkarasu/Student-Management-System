<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Manage Students - Student Management System</title>

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
                    <li class="active"><a href="${pageContext.request.contextPath}/admin/students">Students</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/courses">Courses</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/users">Users</a></li>
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
            <div class="col-md-12">
                <h2><span class="glyphicon glyphicon-user"></span> Manage Students</h2>
                <hr>

                <!-- Messages -->
                <c:if test="${param.success == 'created'}">
                    <div class="alert alert-success alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                        Student created successfully!
                    </div>
                </c:if>
                <c:if test="${param.success == 'updated'}">
                    <div class="alert alert-success alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                        Student updated successfully!
                    </div>
                </c:if>
                <c:if test="${param.success == 'deleted'}">
                    <div class="alert alert-success alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                        Student deleted successfully!
                    </div>
                </c:if>
                <c:if test="${param.error == 'notfound'}">
                    <div class="alert alert-danger alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                        Student not found!
                    </div>
                </c:if>

                <!-- Search and Add Button -->
                <div class="row mb-20">
                    <div class="col-md-6">
                        <form action="${pageContext.request.contextPath}/admin/students" method="get" class="form-inline">
                            <div class="form-group">
                                <input type="text" name="search" class="form-control"
                                       placeholder="Search by name or roll number" value="${search}">
                            </div>
                            <button type="submit" class="btn btn-default">
                                <span class="glyphicon glyphicon-search"></span> Search
                            </button>
                            <c:if test="${not empty search}">
                                <a href="${pageContext.request.contextPath}/admin/students" class="btn btn-default">Clear</a>
                            </c:if>
                        </form>
                    </div>
                    <div class="col-md-6 text-right">
                        <a href="${pageContext.request.contextPath}/admin/student/new" class="btn btn-primary">
                            <span class="glyphicon glyphicon-plus"></span> Add New Student
                        </a>
                    </div>
                </div>

                <!-- Students Table -->
                <div class="panel panel-custom">
                    <div class="panel-heading">
                        <h3 class="panel-title">Students List</h3>
                    </div>
                    <table class="table table-custom table-striped table-hover">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Roll Number</th>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Department</th>
                                <th>Semester</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="student" items="${students}" varStatus="status">
                                <tr>
                                    <td>${status.index + 1}</td>
                                    <td>${student.rollNumber}</td>
                                    <td>${student.name}</td>
                                    <td>${student.email}</td>
                                    <td>${student.department}</td>
                                    <td>${student.semester}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/admin/student/view?id=${student.studentId}"
                                           class="btn btn-info btn-xs" title="View">
                                            <span class="glyphicon glyphicon-eye-open"></span>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/admin/student/edit?id=${student.studentId}"
                                           class="btn btn-warning btn-xs" title="Edit">
                                            <span class="glyphicon glyphicon-pencil"></span>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/admin/student/delete?id=${student.studentId}"
                                           class="btn btn-danger btn-xs" title="Delete"
                                           onclick="return confirm('Are you sure you want to delete this student?');">
                                            <span class="glyphicon glyphicon-trash"></span>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty students}">
                                <tr>
                                    <td colspan="7" class="text-center">No students found.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
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
