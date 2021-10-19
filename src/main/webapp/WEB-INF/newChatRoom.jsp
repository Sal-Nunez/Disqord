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
	<title>New Chat Room</title>
</head>
<body class="bg-dark mt-5 container d-flex justify-content-center" style="color: whitesmoke;">
<div>
	<div>
    <h1>Create a Chat Room</h1>
    <a href="/dashboard" class="mt-2 btn btn-primary">Back to Dashboard</a>
    <form:form class="" action="/newChatRoom" method="post" modelAttribute="chatRoom">
        <div class="form-group">
            <label>Room Name:</label>
            <form:input path="name" class="form-control mb-3" />
            <form:errors path="name" class="text-danger" />
        </div>
        <input type="submit" value="Create Room" class="btn btn-primary" />
    </form:form>
    <div>
    
    <!-- Iterating through Server members to add to server chat room -->
	    <c:forEach var = "serverMember" items = "${ server.serverMembers }">
		      <a href="#" class="btn btn-light mt-1"><c:out value="${ serverMember.serverMember.userName }" /></a>        		
	    </c:forEach>
	    
	<!-- Iterate through all users chat rooms to pick which to display -->	
	    <c:forEach var = "chatRoom" items = "${ user.chatRooms }">
		      <a href="#" class="btn btn-light mt-1"><c:out value="${ chatRoom.name }" /></a>        		
	    </c:forEach>	
    </div>
	</div>
</div>
</body>
</html>