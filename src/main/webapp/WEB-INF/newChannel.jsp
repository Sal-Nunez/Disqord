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
	<title>Create Channel</title>
</head>
<body class="bg-dark mt-5 container d-flex justify-content-center" style="color: whitesmoke;">
<div class="container d-flex justify-content-center flex-column">
	<div>
	    <h1>Create Server Channel</h1>
	    <a href="/dashboard" class="mt-2 btn btn-primary">Back to Dashboard</a>
	    
	    <form:form class="" action="/servers/${server.id}/newChannel" method="post" modelAttribute="channel">
	        <form:input type="hidden" path="server" value="${server.id }"/>
	        <div class="form-group">
	            <label>Channel Name:</label>
	            <form:input path="name" class="form-control mb-3" />
	            <form:errors path="name" class="text-danger" />
	        </div>

	       <div class="form-group">
				<form:select path="category" class="form-control mb-3">
					<form:option value="0">Channel Category (optional)</form:option>
				    <c:forEach var="category" items="${categories}">
					   <form:option value="${category.id}">${category.name }</form:option>
				    </c:forEach>
				</form:select>
	       </div>
	        
	        <input type="submit" value="Create Channel" class="btn btn-primary" />
	    </form:form>
	    
	    <div>
	    <!-- Iterating through Server members to add to server chat room -->
		    <c:forEach var = "serverMember" items = "${ server.serverMembers }">
			      <a href="#" class="btn btn-light mt-1"><c:out value="${ serverMember.serverMember.userName }" /></a>        		
		    </c:forEach>
	    </div>
	    
	</div>
</div>
</body>
</html>