<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import = "java.io.*,java.util.*, javax.servlet.*" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page isErrorPage="true" %>  

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<script type="text/javascript" src="js/t-app.js"></script>
	<!-- for Bootstrap CSS -->
	<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
	<!-- YOUR own local CSS -->
	<link rel="stylesheet" href="/css/style.css" />
	<!-- For any Bootstrap that uses JS or jQuery-->
	<script src="/webjars/jquery/jquery.min.js"></script>
	<script src="/webjars/bootstrap/js/bootstrap.min.js"></script>
	<title>Create Category</title>
</head>
<body class="bg-dark mt-5 container d-flex justify-content-center" style="color: whitesmoke;">
<div class="container d-flex justify-content-center flex-column">
	<div>
	    <h1>Edit ${thisCategory.name} Category</h1>
	    <a href="/dashboard" class="mt-2 btn btn-primary">Back to Dashboard</a>
	    
	    <c:forEach var="oldCategory" items="${categories}">
	    	<c:if test="${oldCategory.server.id == server.id }" >
				<form:form class="" action="/servers/${server.id}/category/${oldCategory.id}/update" method="post" modelAttribute="category">
			        <form:input type="hidden" path="server" value="${server.id }"/>
			        <form:input type="hidden" path="id" value="${oldCategory.id }"/>
			        <div class="form-group">
			            <label>Category Name:</label>
			            <form:input path="name" class="form-control mb-3" value="${oldCategory.name}"/>
			            <form:errors path="name" class="text-danger" />
			        </div>
			        <input type="submit" value="Update Category" class="btn btn-primary" />
			    </form:form>
	    	</c:if>
		    
	    </c:forEach>
	</div>
</div>
</body>
</html>