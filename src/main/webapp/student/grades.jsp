<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>My Grades - Student Management System</title>

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
                    <li class="active"><a href="${pageContext.request.contextPath}/student/grades">My Grades</a></li>
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
                <h2><span class="glyphicon glyphicon-list-alt"></span> My Grades</h2>
                <hr>

                <!-- Error Message -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                        ${error}
                    </div>
                </c:if>

                <!-- Student Info -->
                <div class="row">
                    <div class="col-md-8">
                        <div class="student-info">
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="info-row">
                                        <span class="info-label">Name:</span> ${student.name}
                                    </p>
                                    <p class="info-row">
                                        <span class="info-label">Roll Number:</span> ${student.rollNumber}
                                    </p>
                                </div>
                                <div class="col-md-6">
                                    <p class="info-row">
                                        <span class="info-label">Department:</span> ${student.department}
                                    </p>
                                    <p class="info-row">
                                        <span class="info-label">Semester:</span> ${student.semester}
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="gpa-summary">
                            <div class="gpa-value">${gpa}</div>
                            <div class="gpa-label">Cumulative GPA</div>
                            <div class="mt-10">Total Credits: ${totalCredits}</div>
                        </div>
                    </div>
                </div>

                <!-- Download Transcript -->
                <div class="row mt-20 mb-20">
                    <div class="col-md-12">
                        <a href="${pageContext.request.contextPath}/student/transcript"
                           class="btn btn-success btn-lg">
                            <span class="glyphicon glyphicon-download-alt"></span> Download Transcript (PDF)
                        </a>
                    </div>
                </div>

                <!-- Grades Table -->
                <div class="panel panel-custom">
                    <div class="panel-heading">
                        <h3 class="panel-title">Course Grades</h3>
                    </div>
                    <table class="table table-custom table-striped table-hover">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Course Code</th>
                                <th>Course Name</th>
                                <th>Credits</th>
                                <th>Semester</th>
                                <th>Marks</th>
                                <th>Grade</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="grade" items="${grades}" varStatus="status">
                                <tr>
                                    <td>${status.index + 1}</td>
                                    <td><strong>${grade.courseCode}</strong></td>
                                    <td>${grade.courseName}</td>
                                    <td>${grade.credits}</td>
                                    <td>${grade.semester}</td>
                                    <td>
                                        <fmt:formatNumber value="${grade.marks}" maxFractionDigits="2"/>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty grade.grade}">
                                                <span class="grade-badge grade-${fn:toLowerCase(fn:substring(grade.grade, 0, 1))}">
                                                    ${grade.grade}
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-muted">Not Graded</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty grades}">
                                <tr>
                                    <td colspan="7" class="text-center">No grades available yet.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>

                <!-- Grade Scale Reference -->
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">Grade Points Scale</h4>
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-3">
                                <p><span class="grade-badge grade-a">A+/A</span> = 4.0</p>
                                <p><span class="grade-badge grade-a">A-</span> = 3.7</p>
                            </div>
                            <div class="col-md-3">
                                <p><span class="grade-badge grade-b">B+</span> = 3.3</p>
                                <p><span class="grade-badge grade-b">B</span> = 3.0</p>
                                <p><span class="grade-badge grade-b">B-</span> = 2.7</p>
                            </div>
                            <div class="col-md-3">
                                <p><span class="grade-badge grade-c">C+</span> = 2.3</p>
                                <p><span class="grade-badge grade-c">C</span> = 2.0</p>
                                <p><span class="grade-badge grade-c">C-</span> = 1.7</p>
                            </div>
                            <div class="col-md-3">
                                <p><span class="grade-badge grade-d">D+</span> = 1.3</p>
                                <p><span class="grade-badge grade-d">D</span> = 1.0</p>
                                <p><span class="grade-badge grade-f">F</span> = 0.0</p>
                            </div>
                        </div>
                    </div>
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
