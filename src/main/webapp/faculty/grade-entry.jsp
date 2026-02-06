<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Enter Grades - ${course.courseCode} - Student Management System</title>

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
                <h2><span class="glyphicon glyphicon-edit"></span> Enter Grades</h2>
                <hr>

                <!-- Course Info -->
                <div class="alert alert-info">
                    <strong>Course:</strong> ${course.courseCode} - ${course.courseName}
                    <span class="pull-right"><strong>Credits:</strong> ${course.credits}</span>
                </div>

                <!-- Success Message -->
                <c:if test="${param.success == 'saved'}">
                    <div class="alert alert-success alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                        Grades saved successfully!
                    </div>
                </c:if>

                <!-- Grade Entry Form -->
                <form action="${pageContext.request.contextPath}/faculty/grade/save" method="post">
                    <input type="hidden" name="courseId" value="${course.courseId}">

                    <div class="panel panel-custom">
                        <div class="panel-heading">
                            <h3 class="panel-title">Student Grades</h3>
                        </div>
                        <table class="table table-custom table-striped table-hover grade-table">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Roll Number</th>
                                    <th>Student Name</th>
                                    <th>Semester</th>
                                    <th>Marks (0-100)</th>
                                    <th>Grade</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="grade" items="${grades}" varStatus="status">
                                    <tr>
                                        <td>${status.index + 1}</td>
                                        <td>${grade.studentRollNumber}</td>
                                        <td>${grade.studentName}</td>
                                        <td>${grade.semester}</td>
                                        <td>
                                            <input type="hidden" name="enrollmentId" value="${grade.enrollmentId}">
                                            <input type="number" name="marks" class="form-control marks-input"
                                                   min="0" max="100" step="0.01"
                                                   value="${grade.marks > 0 ? grade.marks : ''}"
                                                   placeholder="0-100"
                                                   data-row="${status.index}">
                                        </td>
                                        <td>
                                            <span class="grade-display" id="grade-${status.index}">
                                                <c:if test="${not empty grade.grade}">
                                                    <span class="grade-badge grade-${fn:toLowerCase(fn:substring(grade.grade, 0, 1))}">
                                                        ${grade.grade}
                                                    </span>
                                                </c:if>
                                            </span>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty grades}">
                                    <tr>
                                        <td colspan="6" class="text-center">No students enrolled in this course.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>

                    <c:if test="${not empty grades}">
                        <div class="form-group">
                            <button type="submit" class="btn btn-primary btn-lg">
                                <span class="glyphicon glyphicon-save"></span> Save All Grades
                            </button>
                            <a href="${pageContext.request.contextPath}/faculty/grades" class="btn btn-default btn-lg">
                                <span class="glyphicon glyphicon-arrow-left"></span> Back to Courses
                            </a>
                        </div>
                    </c:if>
                </form>

                <!-- Grade Scale Reference -->
                <div class="panel panel-default mt-30">
                    <div class="panel-heading">
                        <h4 class="panel-title">Grade Scale Reference</h4>
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-3">
                                <p><span class="grade-badge grade-a">A+</span> 90-100</p>
                                <p><span class="grade-badge grade-a">A</span> 85-89</p>
                                <p><span class="grade-badge grade-a">A-</span> 80-84</p>
                            </div>
                            <div class="col-md-3">
                                <p><span class="grade-badge grade-b">B+</span> 75-79</p>
                                <p><span class="grade-badge grade-b">B</span> 70-74</p>
                                <p><span class="grade-badge grade-b">B-</span> 65-69</p>
                            </div>
                            <div class="col-md-3">
                                <p><span class="grade-badge grade-c">C+</span> 60-64</p>
                                <p><span class="grade-badge grade-c">C</span> 55-59</p>
                                <p><span class="grade-badge grade-c">C-</span> 50-54</p>
                            </div>
                            <div class="col-md-3">
                                <p><span class="grade-badge grade-d">D+</span> 45-49</p>
                                <p><span class="grade-badge grade-d">D</span> 40-44</p>
                                <p><span class="grade-badge grade-f">F</span> Below 40</p>
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
    <script>
        // Auto-calculate grade when marks are entered
        function calculateGrade(marks) {
            if (marks >= 90) return 'A+';
            if (marks >= 85) return 'A';
            if (marks >= 80) return 'A-';
            if (marks >= 75) return 'B+';
            if (marks >= 70) return 'B';
            if (marks >= 65) return 'B-';
            if (marks >= 60) return 'C+';
            if (marks >= 55) return 'C';
            if (marks >= 50) return 'C-';
            if (marks >= 45) return 'D+';
            if (marks >= 40) return 'D';
            return 'F';
        }

        function getGradeClass(grade) {
            var first = grade.charAt(0).toLowerCase();
            return 'grade-' + first;
        }

        $(document).ready(function() {
            $('.marks-input').on('input', function() {
                var row = $(this).data('row');
                var marks = parseFloat($(this).val());
                var gradeSpan = $('#grade-' + row);

                if (!isNaN(marks) && marks >= 0 && marks <= 100) {
                    var grade = calculateGrade(marks);
                    gradeSpan.html('<span class="grade-badge ' + getGradeClass(grade) + '">' + grade + '</span>');
                } else {
                    gradeSpan.html('');
                }
            });
        });
    </script>

    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
</body>
</html>
