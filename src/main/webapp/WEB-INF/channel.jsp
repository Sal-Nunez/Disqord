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
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="/css/main.css"> <!-- change to match your file/naming structure -->
    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/bootstrap/js/bootstrap.min.js"></script>
</head>
<body onload="darkModeCheck()">
    	<!-- Friend Modal -->
	<!-- Note: separate from main content - html is hidden until prompted  -->
            <div class="modal fade" id="search-modal" role="dialog">
                <div class="modal-s modal-dialog lightModeText">
                    <div class="modal-content light-mode">
                        <div class="modal-header">
                            <form class="d-flex align-items-center" id="userSearchForm"> 
                                <input class="form-control me-2" type="search" placeholder="Find a friend" aria-label="userSearch" id="userSearch" name="userSearch"> 
                                <button class="btn btn-outline-success btn-sm" type="submit" id="userSearchBtn">Search</button>
                            </form>
                        </div>
                        <div class="modal-body" id="search-results-container">
                                <!-- AJAX results go here -->
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default lightModeText" data-bs-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
    <div class="container-fluid">
        <nav class="navbar navbar-expand-lg light-mode blr-10 brr-10">
            <div class="container-fluid">
                <a class="navbar-brand lightModeText" href="/dashboard">Disqord</a>
                <button class="navbar-toggler light-mode" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                    aria-expanded="false" aria-label="Toggle navigation">
                    <span class="align-middle  lightModeText" style="font-size: 2rem;">&#9776</span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle lightModeText" href="#" id="navbarDropdown" role="button"
                                data-bs-toggle="dropdown" aria-expanded="false">
                                Account
                            </a>
                            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                                <li><a class="dropdown-item bg-success" href="#" id="navFriendBtn">My Friends</a></li>
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
       	<div class="d-flex">
        	<div class="d-flex flex-row align-items-start">
	        <nav class="navbar navbar-expand-lg light-mode blr-10 brr-10">
                <div class="collapse navbar-collapse align-items-start" id="navbarSupportedContent">
		        	<div class="d-flex flex-column">
			        	<h1 class="me-5">Servers</h1>
			        		<c:forEach var = "server" items = "${ user.servers }">
				        	<a href="/servers/${server.id}" class="btn btn-primary lightModeText mb-3 me-3 button1"><c:out value="${ server.name }" /></a>    		
		        			</c:forEach>
		        			<a href="/servers/new" class="btn btn-primary lightModeText mb-3 me-3 button1 bg-success">+ New Server</a>
		        		</div>
	        		<div class="d-flex flex-column me-5">
       					<h1>Categories</h1>
       					<c:forEach var="channel" items="${ server.channels }">
							<c:if test="${ channel.category.id == null }">
		        				<a href="/servers/${server.id }/channels/${ channel.id }" class="btn btn-primary lightModeText mb-3 me-3 button1"><c:out value="${ channel.name }" /></a>	        
							</c:if>
        				</c:forEach>
       					<c:forEach var="categ" items="${server.categories}">
        					<b class="ps-2" style="font-size: 14pt;">${categ.name }</b>
							<c:forEach var="channel" items="${ server.channels }">
								<c:if test="${categ.id == channel.category.id }">
			        				<a href="/servers/${server.id }/channels/${ channel.id }" class="btn btn-primary lightModeText mb-3 me-3 button1"><c:out value="#${ channel.name }" /></a>	        
								</c:if>
	        				</c:forEach >
     						</c:forEach>
     						<a href="/servers/${server.id}/categories/new" class="btn btn-primary lightModeText mb-3 me-3 button1 bg-success">+ New Server Category</a>
     						<a href="/servers/${server.id}/channels/new" class="btn btn-primary lightModeText mb-3 me-3 button1 bg-success">+ New Channel</a>
	        		</div>
	        	</div>
       		</nav>
       	</div>
   		<div id="chat-page" class="light-mode">
       		<div class="chat-container light-mode mt-2">
           		<div class="chat-header light-mode d-flex justify-content-between">
	                <h2>${user.userName}</h2>
	                <h2 class="light-mode">Welcome to ${channel.name} ${user.fullName}</h2>
	                <a href="#" class="btn btn-outline-light lightModeText" >Invite Friend</a>
           		</div>
            <ul id="messageArea" class="light-mode">
	            <c:forEach var="message" items="${ channel.channelMessages }">
		            <li class="chat-message  lightModeText">
		            	<c:set var="firstLetter" value= "${ fn:substring(message.sender, 0, 1) }" />
		            <i class="${message.sender }" ><c:out value="${ firstLetter }" /></i>
		            <span class="senderName lightModeText"> <c:out value="${message.sender}" /> <span class="time"> <c:out value="${ message.time }" /> <span id="message${ message.id }" class="tooltiptime lightModeText"><c:out value="${ message.floatTime }" /></span></span> </span>
		            <p class=" lightModeText"> <c:out value="${ message.content }" /> </p>            
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
		    <input type="hidden" id="channel_id" value="${ channel.id }" />
		    <input type="hidden" id="user_id" value="${ user.id }" />
		</div>
    </div>
	
	
    <c:if test="${user.id == server.owner.id}">
		<div class="mt-3 d-flex text-center justify-content-center">	
			<a class="mx-2 btn btn-primary" href="/servers/${server.id}/channels/${channel.id}/edit">Edit Channel</a>
			
			<form action="/channels/${channel.id}/delete" method="post">
			    <input type="hidden" name="_method" value="delete">
			    <input type="submit" class="mx-2 btn btn-danger" value="Delete Channel">
			</form>			
		</div>
    </c:if>
	
</body>
<script src="/js/script.js"></script>
<script src="/js/darkMode.js"></script>
<script src="/js/friendSearch.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.4/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
</html>