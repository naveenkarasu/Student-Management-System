<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Dashboard - Student Management System</title>

    <!-- Bootstrap 3 CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <!-- Custom CSS -->
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
                    <li class="active"><a href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>

                    <!-- Admin Menu -->
                    <c:if test="${sessionScope.role == 'admin'}">
                        <li><a href="${pageContext.request.contextPath}/admin/students">Students</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/courses">Courses</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/users">Users</a></li>
                    </c:if>

                    <!-- Faculty Menu -->
                    <c:if test="${sessionScope.role == 'faculty'}">
                        <li><a href="${pageContext.request.contextPath}/faculty/grades">My Courses</a></li>
                    </c:if>

                    <!-- Student Menu -->
                    <c:if test="${sessionScope.role == 'student'}">
                        <li><a href="${pageContext.request.contextPath}/student/grades">My Grades</a></li>
                    </c:if>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                            <span class="glyphicon glyphicon-user"></span>
                            ${sessionScope.username} (${sessionScope.role})
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
        <div class="dashboard-header">
            <h1>Welcome, ${sessionScope.username}!</h1>
            <p>You are logged in as <strong>${sessionScope.role}</strong></p>
        </div>

        <!-- Admin Dashboard -->
        <c:if test="${sessionScope.role == 'admin'}">
            <div class="row">
                <div class="col-md-4">
                    <div class="stat-box primary">
                        <div class="stat-number">${studentCount}</div>
                        <div class="stat-label">Total Students</div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="stat-box success">
                        <div class="stat-number">${courseCount}</div>
                        <div class="stat-label">Total Courses</div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="stat-box warning">
                        <div class="stat-number">${enrollmentCount}</div>
                        <div class="stat-label">Total Enrollments</div>
                    </div>
                </div>
            </div>

            <div class="row mt-30">
                <div class="col-md-6">
                    <div class="panel panel-custom">
                        <div class="panel-heading">
                            <h3 class="panel-title">
                                <span class="glyphicon glyphicon-flash"></span> Quick Actions
                            </h3>
                        </div>
                        <div class="panel-body">
                            <ul class="quick-links">
                                <li><a href="${pageContext.request.contextPath}/admin/student/new">
                                    <span class="glyphicon glyphicon-plus"></span> Add New Student
                                </a></li>
                                <li><a href="${pageContext.request.contextPath}/admin/course/new">
                                    <span class="glyphicon glyphicon-plus"></span> Add New Course
                                </a></li>
                                <li><a href="${pageContext.request.contextPath}/admin/user/new">
                                    <span class="glyphicon glyphicon-plus"></span> Add New User
                                </a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="panel panel-custom">
                        <div class="panel-heading">
                            <h3 class="panel-title">
                                <span class="glyphicon glyphicon-list"></span> Management
                            </h3>
                        </div>
                        <div class="panel-body">
                            <ul class="quick-links">
                                <li><a href="${pageContext.request.contextPath}/admin/students">
                                    <span class="glyphicon glyphicon-user"></span> Manage Students
                                </a></li>
                                <li><a href="${pageContext.request.contextPath}/admin/courses">
                                    <span class="glyphicon glyphicon-book"></span> Manage Courses
                                </a></li>
                                <li><a href="${pageContext.request.contextPath}/admin/users">
                                    <span class="glyphicon glyphicon-cog"></span> Manage Users
                                </a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Faculty Dashboard -->
        <c:if test="${sessionScope.role == 'faculty'}">
            <div class="row">
                <div class="col-md-8">
                    <div class="panel panel-custom">
                        <div class="panel-heading">
                            <h3 class="panel-title">
                                <span class="glyphicon glyphicon-tasks"></span> Faculty Dashboard
                            </h3>
                        </div>
                        <div class="panel-body">
                            <p>Welcome to the Faculty portal. From here you can:</p>
                            <ul class="quick-links">
                                <li><a href="${pageContext.request.contextPath}/faculty/grades">
                                    <span class="glyphicon glyphicon-edit"></span> Enter/Update Grades
                                </a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="panel panel-custom">
                        <div class="panel-heading">
                            <h3 class="panel-title">
                                <span class="glyphicon glyphicon-info-sign"></span> Instructions
                            </h3>
                        </div>
                        <div class="panel-body">
                            <p>To enter grades:</p>
                            <ol>
                                <li>Click on "My Courses"</li>
                                <li>Select a course</li>
                                <li>Enter marks for students</li>
                                <li>Grades will be calculated automatically</li>
                            </ol>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Student Dashboard -->
        <c:if test="${sessionScope.role == 'student'}">
            <div class="row">
                <div class="col-md-8">
                    <div class="panel panel-custom">
                        <div class="panel-heading">
                            <h3 class="panel-title">
                                <span class="glyphicon glyphicon-tasks"></span> Student Dashboard
                            </h3>
                        </div>
                        <div class="panel-body">
                            <p>Welcome to the Student portal. From here you can:</p>
                            <ul class="quick-links">
                                <li><a href="${pageContext.request.contextPath}/student/grades">
                                    <span class="glyphicon glyphicon-list-alt"></span> View My Grades
                                </a></li>
                                <li><a href="${pageContext.request.contextPath}/student/transcript">
                                    <span class="glyphicon glyphicon-download-alt"></span> Download Transcript (PDF)
                                </a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="panel panel-custom">
                        <div class="panel-heading">
                            <h3 class="panel-title">
                                <span class="glyphicon glyphicon-info-sign"></span> Information
                            </h3>
                        </div>
                        <div class="panel-body">
                            <p>Your transcript includes:</p>
                            <ul>
                                <li>All enrolled courses</li>
                                <li>Marks and grades obtained</li>
                                <li>Cumulative GPA calculation</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
    </div>

    <!-- Footer -->
    <div class="footer">
        <div class="container">
            <p>Student Management System &copy; 2018 | Bachelor's Project</p>
        </div>
    </div>

    <!-- jQuery and Bootstrap JS -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>
