<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>404 - Page Not Found</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .error-page {
            text-align: center;
            padding: 100px 20px;
        }
        .error-code {
            font-size: 120px;
            font-weight: bold;
            color: #e74c3c;
        }
        .error-message {
            font-size: 24px;
            color: #7f8c8d;
            margin-bottom: 30px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="error-page">
            <div class="error-code">404</div>
            <div class="error-message">Oops! Page Not Found</div>
            <p class="text-muted">The page you are looking for might have been removed, had its name changed, or is temporarily unavailable.</p>
            <br>
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary btn-lg">
                <span class="glyphicon glyphicon-home"></span> Go to Dashboard
            </a>
            <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-default btn-lg">
                <span class="glyphicon glyphicon-log-in"></span> Login Page
            </a>
        </div>
    </div>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>
