<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${empty course ? 'Add New' : 'Edit'} Course - Student Management System</title>

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
                <h2>
                    <span class="glyphicon glyphicon-${empty course ? 'plus' : 'pencil'}"></span>
                    ${empty course ? 'Add New Course' : 'Edit Course'}
                </h2>
                <hr>

                <!-- Error Message -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                        ${error}
                    </div>
                </c:if>

                <!-- Course Form -->
                <div class="form-custom">
                    <form action="${pageContext.request.contextPath}/admin/course/${empty course ? 'save' : 'update'}"
                          method="post" class="form-horizontal">

                        <c:if test="${not empty course}">
                            <input type="hidden" name="courseId" value="${course.courseId}">
                        </c:if>

                        <div class="form-group">
                            <label for="courseCode" class="col-sm-3 control-label">Course Code *</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" id="courseCode" name="courseCode"
                                       value="${course.courseCode}" required placeholder="e.g., CS101">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="courseName" class="col-sm-3 control-label">Course Name *</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" id="courseName" name="courseName"
                                       value="${course.courseName}" required placeholder="Enter course name">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="credits" class="col-sm-3 control-label">Credits *</label>
                            <div class="col-sm-9">
                                <select class="form-control" id="credits" name="credits" required>
                                    <option value="">Select Credits</option>
                                    <c:forEach begin="1" end="6" var="credit">
                                        <option value="${credit}" ${course.credits == credit ? 'selected' : ''}>${credit}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="facultyId" class="col-sm-3 control-label">Assign Faculty</label>
                            <div class="col-sm-9">
                                <select class="form-control" id="facultyId" name="facultyId">
                                    <option value="">-- Not Assigned --</option>
                                    <c:forEach var="faculty" items="${facultyList}">
                                        <option value="${faculty.facultyId}"
                                            ${course.facultyId == faculty.facultyId ? 'selected' : ''}>
                                            ${faculty.name} (${faculty.department})
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <hr>

                        <div class="form-group">
                            <div class="col-sm-offset-3 col-sm-9">
                                <button type="submit" class="btn btn-primary">
                                    <span class="glyphicon glyphicon-save"></span>
                                    ${empty course ? 'Save Course' : 'Update Course'}
                                </button>
                                <a href="${pageContext.request.contextPath}/admin/courses" class="btn btn-default">
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
