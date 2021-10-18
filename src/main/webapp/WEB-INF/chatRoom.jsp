<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- c:out ; c:forEach etc. -->
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- Formatting (dates) -->
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!-- form:form -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- for rendering errors on PUT routes -->
<%@ page isErrorPage="true" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="/css/style.css"> <!-- change to match your file/naming structure -->
    <link rel="stylesheet" href="/css/main.css">
    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/bootstrap/js/bootstrap.min.js"></script>
</head>
<body onload="darkModeCheck()">
    <div class="container">
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container-fluid">
                <a class="navbar-brand" href="/dashboard">Dashboard</a>
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
  						<input class="form-check-input" type="checkbox" role="switch" id="flexSwitchCheckChecked" onClick="darkMode(); darkModeCheck();">
  						<label class="form-check-label lightModeText" for="flexSwitchCheckChecked">Dark Mode</label>
					</div>
                    <form class="d-flex">
                        <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
                        <button class="btn btn-outline-success" type="submit">Search</button>
                    </form>
                </div>
            </div>
        </nav>
        <div class="row mt-4">
        	<div class="col-1" id="servers">
        		<ul class="icon-list">
	        		<c:forEach var = "server" items = "${ user.servers }">
		        		<li class="icon-item">
			        		<a href="#" class="btn rounded-circle border border-white icon-link mb-2"><c:out value="${ server.name }" /></a>
			        	</li>     		
	        		</c:forEach>
	        		<li class="icon-item">
		        		<a href="servers/new" class="btn btn-success mt-1 rounded-circle icon-link">+</a>
		        	</li>
        		</ul>
        		<c:forEach var = "server" items = "${ user.serverMembers }">
	        	<a href="#" class="btn btn-light mt-1"><c:out value="${ server.server.name }" /></a>        		
        		</c:forEach>
        	</div>
	        <div class="main col-2 ms-4" id="chats">
	        <c:forEach var="chatRoom" items="${ user.chatRooms }">
	        	<a href="/chatRooms/${ chatRoom.id }" class="btn btn-light mt-1"><c:out value="${ chatRoom.name }" /></a>	        
	        </c:forEach>
	        	<a href="/chatRooms/new" class="btn btn-light mt-1">New Chat Room</a>	        
	        </div>
    <div id="chat-page" class="bg-dark col-8">
        <div class="chat-container bg-dark">
            <div class="chat-header bg-dark">
                <h2 class="bg-dark">Welcome to ${chatRoom.name}</h2>
            </div>
            <ul id="messageArea" class="bg-dark">
            <c:forEach var="message" items="${ chatRoom.chatMessages }">
            <li class="chat-message  text-white">
            <c:set var="firstLetter" value= "${ fn:substring(message.sender, 0, 1) }" />
            <i class="${message.sender }" ><c:out value="${ firstLetter }" /></i>
            <span class="senderName text-white"> <c:out value="${message.sender}" /> <span class="time"> <c:out value="${ message.time }" /> <span id="message${ message.id }" class="tooltiptime text-white"><c:out value="${ message.floatTime }" /></span></span> </span>
            <p class=" text-white"> <c:out value="${ message.content }" /> </p>            
            </li>
            </c:forEach>
            </ul>
            <form id="messageForm" name="messageForm">
                <div class="form-group">
                    <div class="input-group clearfix">
                        <input type="text" id="message" placeholder="Type a message..." autocomplete="off" class="form-control" />
                        <button type="submit" class="primary">Send</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <input type="hidden" id="name" value="${ user.userName }" />
    <input type="hidden" id="chat_room_id" value="${ chatRoom.id }" />
    <input type="hidden" id="user_id" value="${ user.id }" />
        </div>
	</div>
</body>
<script src="/js/darkMode.js"></script>
<script src="/js/script.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.4/sockjs.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
</html>