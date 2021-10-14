<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- c:out ; c:forEach etc. -->
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- Formatting (dates) -->
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!-- form:form -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!-- for rendering errors on PUT routes -->
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>Disqord</title>
    <link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
    <link rel="stylesheet" href="/css/main.css" />
    <!-- change to match your file/naming structure -->
    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/bootstrap/js/bootstrap.min.js"></script>
</head>

<body class="bg-dark" style="color: #DADDD8">
    <div class="container my-5 text-center">
        <h1 style="color: #DADDD8">Disqord</h1>
        <p>Login/Registration</p>
    </div>
    <div class="d-flex justify-content-center">
        <form:form class="col-5" action="/register" method="post" modelAttribute="newUser">
            <div class="form-group">
                <label>First Name:</label>
                <form:input path="firstName" class="form-control mb-3" />
                <form:errors path="firstName" class="text-danger" />
            </div>
            <div class="form-group">
                <label>Last Name:</label>
                <form:input path="lastName" class="form-control mb-3" />
                <form:errors path="lastName" class="text-danger" />
            </div>
            <div class="form-group">
                <label>Username</label>
                <form:input path="userName" class="form-control mb-3" />
                <form:errors path="userName" class="text-danger" />
            </div>
            <div class="form-group">
                <label>Email:</label>
                <form:input path="email" class="form-control mb-3" />
                <form:errors path="email" class="text-danger" />
            </div>
            <div class="form-group">
                <label>Password:</label>
                <form:password path="password" class="form-control mb-3" />
                <form:errors path="password" class="text-danger" />
            </div>
            <div class="form-group">
                <label>Confirm Password:</label>
                <form:password path="confirm" class="form-control mb-3" />
                <form:errors path="confirm" class="text-danger" />
            </div>
            <input type="submit" value="Register" class="btn btn-primary" />
        </form:form>
		<div class="col-1"></div>
        <form:form class="col-5" action="/login" method="post" modelAttribute="newLogin">
            <div class="form-group">
                <label>Email:</label>
                <form:input path="email" class="form-control mb-3" />
                <form:errors path="email" class="text-danger" />
            </div>
            <div class="form-group">
                <label>Password:</label>
                <form:password path="password" class="form-control mb-3" />
                <form:errors path="password" class="text-danger" />
            </div>
            <input type="submit" value="Login" class="btn btn-success" />
        </form:form>
    </div>
</body>
</html>