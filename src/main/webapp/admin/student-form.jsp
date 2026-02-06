<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${empty student ? 'Add New' : 'Edit'} Student - Student Management System</title>

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
            <div class="col-md-8 col-md-offset-2">
                <h2>
                    <span class="glyphicon glyphicon-${empty student ? 'plus' : 'pencil'}"></span>
                    ${empty student ? 'Add New Student' : 'Edit Student'}
                </h2>
                <hr>

                <!-- Error Message -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                        ${error}
                    </div>
                </c:if>

                <!-- Student Form -->
                <div class="form-custom">
                    <form action="${pageContext.request.contextPath}/admin/student/${empty student ? 'save' : 'update'}"
                          method="post" class="form-horizontal">

                        <c:if test="${not empty student}">
                            <input type="hidden" name="studentId" value="${student.studentId}">
                        </c:if>

                        <div class="form-group">
                            <label for="rollNumber" class="col-sm-3 control-label">Roll Number *</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" id="rollNumber" name="rollNumber"
                                       value="${student.rollNumber}" required placeholder="e.g., CS2018001">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="name" class="col-sm-3 control-label">Full Name *</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" id="name" name="name"
                                       value="${student.name}" required placeholder="Enter full name">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="email" class="col-sm-3 control-label">Email</label>
                            <div class="col-sm-9">
                                <input type="email" class="form-control" id="email" name="email"
                                       value="${student.email}" placeholder="Enter email address">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="department" class="col-sm-3 control-label">Department *</label>
                            <div class="col-sm-9">
                                <select class="form-control" id="department" name="department" required>
                                    <option value="">Select Department</option>
                                    <option value="Computer Science" ${student.department == 'Computer Science' ? 'selected' : ''}>Computer Science</option>
                                    <option value="Information Technology" ${student.department == 'Information Technology' ? 'selected' : ''}>Information Technology</option>
                                    <option value="Electronics" ${student.department == 'Electronics' ? 'selected' : ''}>Electronics</option>
                                    <option value="Electrical" ${student.department == 'Electrical' ? 'selected' : ''}>Electrical</option>
                                    <option value="Mechanical" ${student.department == 'Mechanical' ? 'selected' : ''}>Mechanical</option>
                                    <option value="Civil" ${student.department == 'Civil' ? 'selected' : ''}>Civil</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="semester" class="col-sm-3 control-label">Semester *</label>
                            <div class="col-sm-9">
                                <select class="form-control" id="semester" name="semester" required>
                                    <option value="">Select Semester</option>
                                    <c:forEach begin="1" end="8" var="sem">
                                        <option value="${sem}" ${student.semester == sem ? 'selected' : ''}>${sem}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <!-- Create User Account Section (only for new students) -->
                        <c:if test="${empty student}">
                            <hr>
                            <h4>User Account (Optional)</h4>

                            <div class="form-group">
                                <label class="col-sm-3 control-label">Create User Account?</label>
                                <div class="col-sm-9">
                                    <label class="radio-inline">
                                        <input type="radio" name="createUser" value="yes" id="createUserYes"> Yes
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="createUser" value="no" checked> No
                                    </label>
                                </div>
                            </div>

                            <div id="userAccountFields" style="display: none;">
                                <div class="form-group">
                                    <label for="username" class="col-sm-3 control-label">Username</label>
                                    <div class="col-sm-9">
                                        <input type="text" class="form-control" id="username" name="username"
                                               placeholder="Enter username">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="password" class="col-sm-3 control-label">Password</label>
                                    <div class="col-sm-9">
                                        <input type="password" class="form-control" id="password" name="password"
                                               placeholder="Enter password">
                                    </div>
                                </div>
                            </div>
                        </c:if>

                        <hr>

                        <div class="form-group">
                            <div class="col-sm-offset-3 col-sm-9">
                                <button type="submit" class="btn btn-primary">
                                    <span class="glyphicon glyphicon-save"></span>
                                    ${empty student ? 'Save Student' : 'Update Student'}
                                </button>
                                <a href="${pageContext.request.contextPath}/admin/students" class="btn btn-default">
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
    <script>
        $(document).ready(function() {
            $('input[name="createUser"]').change(function() {
                if ($('#createUserYes').is(':checked')) {
                    $('#userAccountFields').slideDown();
                    $('#username').prop('required', true);
                    $('#password').prop('required', true);
                } else {
                    $('#userAccountFields').slideUp();
                    $('#username').prop('required', false);
                    $('#password').prop('required', false);
                }
            });
        });
    </script>
</body>
</html>
