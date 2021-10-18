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
    <title>Title</title>
    <link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <link rel="stylesheet" href="/css/main.css"> <!-- change to match your file/naming structure -->
    <link rel="stylesheet" href="/css/style.css">
    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/bootstrap/js/bootstrap.min.js"></script>
</head>
<body class="bg-dark" style="color: whitesmoke;">
   <noscript>
      <h2>Sorry! Your browser doesn't support Javascript</h2>
    </noscript>

    <div id="chat-page" class="bg-dark">
        <div class="chat-container bg-dark">
            <div class="chat-header bg-dark">
                <h2 class="bg-dark">Spring WebSocket Chat Demo</h2>
                <a href="/chatRooms/new" class="btn btn-sm btn-outline-dark">New Chat Room</a>
            </div>
            <div class="connecting">
                Connecting...
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
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.4/sockjs.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
    <script src="/js/script.js"></script>
</body>
</html>