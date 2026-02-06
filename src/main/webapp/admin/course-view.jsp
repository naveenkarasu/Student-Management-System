<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>View Course - Student Management System</title>

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
            <div class="col-md-12">
                <h2><span class="glyphicon glyphicon-book"></span> Course Details</h2>
                <hr>

                <!-- Messages -->
                <c:if test="${param.success == 'enrolled'}">
                    <div class="alert alert-success alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                        Student enrolled successfully!
                    </div>
                </c:if>
                <c:if test="${param.success == 'unenrolled'}">
                    <div class="alert alert-success alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                        Student unenrolled successfully!
                    </div>
                </c:if>
                <c:if test="${param.error == 'alreadyenrolled'}">
                    <div class="alert alert-danger alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                        Student is already enrolled in this course for the specified semester!
                    </div>
                </c:if>

                <!-- Course Info -->
                <div class="panel panel-custom">
                    <div class="panel-heading">
                        <h3 class="panel-title">${course.courseCode} - ${course.courseName}</h3>
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-4">
                                <p><strong>Course Code:</strong> ${course.courseCode}</p>
                            </div>
                            <div class="col-md-4">
                                <p><strong>Credits:</strong> ${course.credits}</p>
                            </div>
                            <div class="col-md-4">
                                <p><strong>Faculty:</strong> ${course.facultyName != null ? course.facultyName : 'Not Assigned'}</p>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Enrolled Students -->
                <div class="row mb-20">
                    <div class="col-md-6">
                        <h4>Enrolled Students</h4>
                    </div>
                    <div class="col-md-6 text-right">
                        <a href="${pageContext.request.contextPath}/admin/course/enroll?id=${course.courseId}"
                           class="btn btn-primary btn-sm">
                            <span class="glyphicon glyphicon-plus"></span> Enroll Student
                        </a>
                    </div>
                </div>

                <div class="panel panel-custom">
                    <table class="table table-custom table-striped table-hover">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Roll Number</th>
                                <th>Student Name</th>
                                <th>Semester</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="enrollment" items="${enrollments}" varStatus="status">
                                <tr>
                                    <td>${status.index + 1}</td>
                                    <td>${enrollment.studentRollNumber}</td>
                                    <td>${enrollment.studentName}</td>
                                    <td>${enrollment.semester}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/admin/course/unenroll?enrollmentId=${enrollment.enrollmentId}&courseId=${course.courseId}"
                                           class="btn btn-danger btn-xs"
                                           onclick="return confirm('Are you sure you want to unenroll this student?');">
                                            <span class="glyphicon glyphicon-remove"></span> Unenroll
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty enrollments}">
                                <tr>
                                    <td colspan="5" class="text-center">No students enrolled.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>

                <a href="${pageContext.request.contextPath}/admin/courses" class="btn btn-default">
                    <span class="glyphicon glyphicon-arrow-left"></span> Back to Courses
                </a>
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
