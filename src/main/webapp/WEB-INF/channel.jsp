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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="/css/style.css"> <!-- change to match your file/naming structure -->
    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/bootstrap/js/bootstrap.min.js"></script>
</head>
<body>
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
                    <div class="form-check form-switch me-4">
  						<input class="form-check-input" type="checkbox" role="switch" id="flexSwitchCheckChecked" onClick="darkMode()">
  						<label class="form-check-label darkModeText" for="flexSwitchCheckChecked">Dark Mode</label>
					</div>
                    <form class="d-flex">
                        <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
                        <button class="btn btn-outline-success" type="submit">Search</button>
                    </form>
                </div>
            </div>
        </nav>
        
        
        <div class="main row">
        	<div class="col-1" id="servers">
        		<c:forEach var = "server" items = "${ user.servers }">
	        	<a href="/servers/${ server.id }" class="btn btn-light mt-1"><c:out value="${ server.name }" /></a>        		
        		</c:forEach>
        		<c:forEach var = "server" items = "${ user.serverMembers }">
	        	<a href="#" class="btn btn-light mt-1"><c:out value="${ server.server.name }" /></a>        		
        		</c:forEach>
	        	<a href="/servers/new" class="btn btn-success mt-1">+ Add a server</a>
        	</div>
	        <div class="main col-1 ms-4" id="channels">
	        	<c:forEach var="channel" items="${ server.channels }">
		        	<a href="/servers/${server.id }/channels/${ channel.id }" class="btn btn-light mt-1"><c:out value="${ channel.name }" /></a>	        
		        </c:forEach>
		        	<a href="/servers/${server.id}/channels/new" class="btn btn-light mt-1">New Channel</a>	        
	        </div>
	        
	        
    <!-- Start of main portion -->
	        <div class="col-5 ms-4 bg-warning" id="main">
	        	<h1>${channel.name }</h1>
	        </div>
    <!-- ^^ end of main  -->





        </div>
        
        
        
	</div>
</body>
<script src="/js/script.js"></script>
</html>