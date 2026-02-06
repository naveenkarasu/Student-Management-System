<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>My Courses - Student Management System</title>

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
                    <li class="active"><a href="${pageContext.request.contextPath}/faculty/grades">My Courses</a></li>
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
                <h2><span class="glyphicon glyphicon-book"></span> My Courses</h2>
                <p class="text-muted">Welcome, ${faculty.name}! Select a course to enter/update grades.</p>
                <hr>

                <!-- Error Message -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                        ${error}
                    </div>
                </c:if>
                <c:if test="${param.error == 'unauthorized'}">
                    <div class="alert alert-danger alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                        You are not authorized to access that course!
                    </div>
                </c:if>

                <!-- Courses List -->
                <div class="row">
                    <c:forEach var="course" items="${courses}">
                        <div class="col-md-4">
                            <div class="panel panel-custom">
                                <div class="panel-heading">
                                    <h3 class="panel-title">${course.courseCode}</h3>
                                </div>
                                <div class="panel-body">
                                    <h4>${course.courseName}</h4>
                                    <p><strong>Credits:</strong> ${course.credits}</p>
                                </div>
                                <div class="panel-footer">
                                    <a href="${pageContext.request.contextPath}/faculty/grade/course?id=${course.courseId}"
                                       class="btn btn-primary btn-block">
                                        <span class="glyphicon glyphicon-edit"></span> Enter Grades
                                    </a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                    <c:if test="${empty courses}">
                        <div class="col-md-12">
                            <div class="alert alert-info">
                                <span class="glyphicon glyphicon-info-sign"></span>
                                No courses assigned to you. Please contact the administrator.
                            </div>
                        </div>
                    </c:if>
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
