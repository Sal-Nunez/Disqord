<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- c:out ; c:forEach etc. --> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- Formatting (dates) --> 
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!-- form:form -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- for rendering errors on PUT routes -->
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="/css/style.css"> <!-- change to match your file/naming structure -->
    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/bootstrap/js/bootstrap.min.js"></script>
</head>
<body class="bg-dark" style="color: whitesmoke;">
    <div class="container">
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">Dashboard</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                    aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                                data-bs-toggle="dropdown" aria-expanded="false">
                                Dropdown
                            </a>
                            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                                <li><a class="dropdown-item bg-success" href="#">Add Friend</a></li>
                                <li><a class="dropdown-item bg-danger" href="/logout">Logout</a></li>
                                <li>
                            </ul>
                        </li>
                    </ul>
                    <form class="d-flex">
                        <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
                        <button class="btn btn-outline-success" type="submit">Search</button>
                    </form>
                </div>
            </div>
        </nav>
        <div class="main row">
        	<div class="col-1" id="servers">
	        	<a href="#" class="btn btn-light mt-1">Server Example</a>
	        	<a href="#" class="btn btn-light mt-1">Server Example</a>
	        	<a href="#" class="btn btn-light mt-1">Server Example</a>
	        	<a href="#" class="btn btn-light mt-1">Server Example</a>
	        	<a href="#" class="btn btn-light mt-1">Server Example</a>
	        	<a href="#" class="btn btn-light mt-1">Server Example</a>
	        	<a href="#" class="btn btn-light mt-1">Server Example</a>
	        	<a href="#" class="btn btn-light mt-1">Server Example</a>
	        	<a href="#" class="btn btn-light mt-1">Server Example</a>
	        	<a href="#" class="btn btn-light mt-1">Server Example</a>
	        	<a href="#" class="btn btn-light mt-1">Server Example</a>
	        	<a href="#" class="btn btn-success mt-1">+ Add Server Example</a>
        	</div>
	        <div class="main col-1" id="chats">
	        	<a href="#" class="btn btn-light mt-1">Chat Example</a>
	        	<a href="#" class="btn btn-light mt-1">Chat Example</a>
	        	<a href="#" class="btn btn-light mt-1">Chat Example</a>
	        	<a href="#" class="btn btn-light mt-1">Chat Example</a>
	        </div>
	        <div class="col-10" id="main">
	        	<h1>Chat Messages</h1>
				<div class="container1">
				  <img src="/w3images/bandmember.jpg" alt="Avatar">
				  <p>Hello. How are you today?</p>
				  <span class="time-right">11:00</span>
				</div>
				
				<div class="container1 darker">
				  <img src="/w3images/avatar_g2.jpg" alt="Avatar" class="right">
				  <p>Hey! I'm fine. Thanks for asking!</p>
				  <span class="time-left">11:01</span>
				</div>
				
				<div class="container1">
				  <img src="/w3images/bandmember.jpg" alt="Avatar">
				  <p>Sweet! So, what do you wanna do today?</p>
				  <span class="time-right">11:02</span>
				</div>
				
				<div class="container1 darker">
				  <img src="/w3images/avatar_g2.jpg" alt="Avatar" class="right">
				  <p>Nah, I dunno. Play soccer.. or learn more coding perhaps?</p>
				  <span class="time-left">11:05</span>
				</div>
	        </div>
        </div>
	</div>
</body>
</html>