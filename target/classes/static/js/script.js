'use strict';
console.log("test", document)
var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var i = document.querySelectorAll("i");
var timeFloaters = document.querySelectorAll(".time")
messageArea.scrollTop = messageArea.scrollHeight;

timeFloaters.forEach(e => {
	e.addEventListener("mouseover", () => {
		tooltiptime(e);
	})
})

timeFloaters.forEach(e => {
	e.addEventListener("mouseout", () => {
		tooltiptimemouseout(e);
	})
})


for(let x=0; x<i.length; x++){
	let name = i[x].className
	i[x].style['background-color'] = getAvatarColor(name.toString());
}

var stompClient = null;
var username = null;
var user_id = null;
var chat_room_id = null;
var channel_id = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function tooltiptime(e) {
	wait(1000);
	console.log(e.children[0])
	e.children[0].classList.add("tooltiptimevisible");
}

function wait(ms){
   var start = new Date().getTime();
   var end = start;
   while(end < start + ms) {
     end = new Date().getTime();
  }
}

function tooltiptimemouseout(e){
	e.children[0].classList.remove("tooltiptimevisible");
}

function connect(event) {
    username = document.querySelector('#name').value.trim();
    user_id = document.querySelector('#user_id').value.trim();
    if(document.querySelector("#chat_room_id")){
    chat_room_id = document.querySelector('#chat_room_id').value.trim();	
    }
    if(document.querySelector("#channel_id")){
    channel_id = document.querySelector('#channel_id').value.trim();	
    }
    console.log(username, "***********************");
    if(username) {

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    // Subscribe to the Public Topic
    if(channel_id){
	console.log("there is no channel id though right")
	} else if (chat_room_id){
    stompClient.subscribe('/topic/public/chat_room/' + chat_room_id, onMessageReceived);		
	}

    // Tell your username to the server

}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT',
            user_id: user_id,
            chat_room_id: chat_room_id
        };
    if (document.querySelector("#chat_room_id")){
	console.log("we in here")
    stompClient.send("/app/chat.sendMessage/chat_room/" + chat_room_id, {}, JSON.stringify(chatMessage));	
    }
    messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        messageElement.classList.add('text-white');
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        messageElement.classList.add('text-white');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');
        messageElement.classList.add('text-white');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender + " ");
        usernameElement.appendChild(usernameText);
        usernameElement.classList.add('text-white');
        messageElement.appendChild(usernameElement);
        var time = document.createTextNode(message.time);
        var timeElement = document.createElement('span');
        usernameElement.appendChild(timeElement);
        timeElement.appendChild(time);
        timeElement.classList.add("time");
        var timeFloat = document.createTextNode(message.floatTime);
        var timeFloatElement = document.createElement('span');
        timeElement.appendChild(timeFloatElement);
        timeFloatElement.appendChild(timeFloat);
        timeFloatElement.classList.add("tooltiptime");
        
        timeElement.addEventListener("mouseover", () => {
		tooltiptime(timeElement);
	})
        
        timeElement.addEventListener("mouseout", () => {
		tooltiptimemouseout(timeElement);
		
	})
    }

    var textElement = document.createElement('p');
    textElement.classList.add('text-white');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
	var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}
window.onload = connect;
messageForm.addEventListener('submit', sendMessage, true)
