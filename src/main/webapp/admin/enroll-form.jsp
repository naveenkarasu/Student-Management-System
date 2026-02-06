<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Enroll Student - Student Management System</title>

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
                    <li class="active"><a href="${pageContext.request.contextPath}/admin/courses">Courses</a></li>
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
            <div class="col-md-8 col-md-offset-2">
                <h2><span class="glyphicon glyphicon-plus"></span> Enroll Student</h2>
                <hr>

                <div class="alert alert-info">
                    <strong>Course:</strong> ${course.courseCode} - ${course.courseName}
                </div>

                <!-- Enrollment Form -->
                <div class="form-custom">
                    <form action="${pageContext.request.contextPath}/admin/course/enroll"
                          method="post" class="form-horizontal">

                        <input type="hidden" name="courseId" value="${course.courseId}">

                        <div class="form-group">
                            <label for="studentId" class="col-sm-3 control-label">Select Student *</label>
                            <div class="col-sm-9">
                                <select class="form-control" id="studentId" name="studentId" required>
                                    <option value="">-- Select Student --</option>
                                    <c:forEach var="student" items="${students}">
                                        <option value="${student.studentId}">
                                            ${student.rollNumber} - ${student.name}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="semester" class="col-sm-3 control-label">Semester *</label>
                            <div class="col-sm-9">
                                <select class="form-control" id="semester" name="semester" required>
                                    <option value="">-- Select Semester --</option>
                                    <option value="Fall 2018">Fall 2018</option>
                                    <option value="Spring 2018">Spring 2018</option>
                                    <option value="Fall 2017">Fall 2017</option>
                                    <option value="Spring 2017">Spring 2017</option>
                                </select>
                            </div>
                        </div>

                        <hr>

                        <div class="form-group">
                            <div class="col-sm-offset-3 col-sm-9">
                                <button type="submit" class="btn btn-primary">
                                    <span class="glyphicon glyphicon-ok"></span> Enroll Student
                                </button>
                                <a href="${pageContext.request.contextPath}/admin/course/view?id=${course.courseId}"
                                   class="btn btn-default">
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
