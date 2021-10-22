'use strict';
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


for (let x = 0; x < i.length; x++) {
    let name = i[x].className
    i[x].style['background-color'] = getAvatarColor(name.toString());
}

var stompClient = null;
var username = document.querySelector('#name').value.trim();
var user_id = null;
var chat_room_id = null;
var channel_id = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function tooltiptime(e) {
    wait(1000);
    e.children[0].classList.add("tooltiptimevisible");
}

function wait(ms) {
    var start = new Date().getTime();
    var end = start;
    while (end < start + ms) {
        end = new Date().getTime();
    }
}

function tooltiptimemouseout(e) {
    e.children[0].classList.remove("tooltiptimevisible");
}

function connect(event) {
    user_id = document.querySelector('#user_id').value.trim();
    if (document.querySelector("#chat_room_id")) {
        chat_room_id = document.querySelector('#chat_room_id').value.trim();
    }
    if (document.querySelector("#channel_id")) {
        channel_id = document.querySelector('#channel_id').value.trim();
    }
    if (username) {

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    // Subscribe to the Public Topic
	let chatRoomListeners = document.querySelectorAll(".ChatRoomListener")
	let channelListeners = document.querySelectorAll(".ChannelListener")
	
	chatRoomListeners.forEach(e => {
		let chatRoomListenerId = e.getAttribute("id")
		let chat_room_notification_id = chatRoomListenerId.substring(16, chatRoomListenerId.length)
		stompClient.subscribe('/topic/notification/chat_room/' + chat_room_notification_id, onNotificationReceived)	
	})
	let channel_notification_id = null;
	if (channel_notification_id) {
	stompClient.subscribe('/topic/notification/channel/' + channel_notification_id, onNotificationReceived)		
	}

    if (channel_id) {
        stompClient.subscribe('/topic/public/channel/' + channel_id, onMessageReceived);
    } else if (chat_room_id) {
        stompClient.subscribe('/topic/public/chat_room/' + chat_room_id, onMessageReceived);
    }

    // Tell your username to the server

}


function onError(error) {
	console.log(error);
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT',
            user_id: user_id,
            chat_room_id: chat_room_id,
            channel_id: channel_id
        };
        if (chat_room_id) {
	        let notification = {
				count: 1,
				chat_room_id: chat_room_id,
				user_id: user_id
			}
			stompClient.send("/app/sendNotification/chat_room/" + chat_room_id, {}, JSON.stringify(notification))
            stompClient.send("/app/chat.sendMessage/chat_room/" + chat_room_id, {}, JSON.stringify(chatMessage));
        } else if (channel_id) {
			stompClient.send("/app/sendNotification/channel/" + channel_id, {}, JSON.stringify(notification))
            stompClient.send("/app/chat.sendMessage/channel/" + channel_id, {}, JSON.stringify(chatMessage));
        }
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    if (message.type === 'JOIN') {
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
        var textElement = document.createElement('p');
        if (localStorage.getItem('theme') == "light-mode") {
            textElement.classList.add('lightModeText');
            usernameElement.classList.add('lightModeText');
        } else {
            textElement.classList.add('lightModeText');
            usernameElement.classList.add('lightModeText');
            textElement.classList.add('darkModeText');
            usernameElement.classList.add('darkModeText');
        }
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

    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function onNotificationReceived(payload){
	let receivedNotificaiton = JSON.parse(payload.body);
	fetch("http://localhost:8080/chat_room_notifications/" + receivedNotificaiton.chat_room_id + "/" + user_id) // fetch returns a promise
	.then(res => res.json()) // this also returns promise
	.then(data => {
		if ("http://localhost:8080/chatRooms/" + receivedNotificaiton.chat_room_id == window.location.href){
			fetch("http://localhost:8080/chat_room_notifications/" + receivedNotificaiton.chat_room_id + "/" + user_id + "/reset")
			.then(res => res.json())
			.then (data1 => {
			let displayNotification = document.querySelector(`#chatRoomListener${receivedNotificaiton.chat_room_id}`)
			displayNotification.innerText = ""			
			}) 
		} else {
			let displayNotification = document.querySelector(`#chatRoomListener${receivedNotificaiton.chat_room_id}`)
			displayNotification.innerText = data.count
		}
		
	})
}

function getAvatarColor(messageSender) {
    var colors = [
        '#2196F3', '#32c787', '#00BCD4', '#ff5652',
        '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
    ];
    var moreColors = ["#63b598", "#ce7d78", "#ea9e70", "#a48a9e", "#c6e1e8", "#648177" ,"#0d5ac1" ,
"#f205e6" ,"#1c0365" ,"#14a9ad" ,"#4ca2f9" ,"#a4e43f" ,"#d298e2" ,"#6119d0",
"#d2737d" ,"#c0a43c" ,"#f2510e" ,"#651be6" ,"#79806e" ,"#61da5e" ,"#cd2f00" ,
"#9348af" ,"#01ac53" ,"#c5a4fb" ,"#996635","#b11573" ,"#4bb473" ,"#75d89e" ,
"#2f3f94" ,"#2f7b99" ,"#da967d" ,"#34891f" ,"#b0d87b" ,"#ca4751" ,"#7e50a8" ,
"#c4d647" ,"#e0eeb8" ,"#11dec1" ,"#289812" ,"#566ca0" ,"#ffdbe1" ,"#2f1179" ,
"#935b6d" ,"#916988" ,"#513d98" ,"#aead3a", "#9e6d71", "#4b5bdc", "#0cd36d",
"#250662", "#cb5bea", "#228916", "#ac3e1b", "#df514a", "#539397", "#880977",
"#f697c1", "#ba96ce", "#679c9d", "#c6c42c", "#5d2c52", "#48b41b", "#e1cf3b",
"#5be4f0", "#57c4d8", "#a4d17a", "#225b8", "#be608b", "#96b00c", "#088baf",
"#f158bf", "#e145ba", "#ee91e3", "#05d371", "#5426e0", "#4834d0", "#802234",
"#6749e8", "#0971f0", "#8fb413", "#b2b4f0", "#c3c89d", "#c9a941", "#41d158",
"#fb21a3", "#51aed9", "#5bb32d", "#807fb", "#21538e", "#89d534", "#d36647",
"#7fb411", "#0023b8", "#3b8c2a", "#986b53", "#f50422", "#983f7a", "#ea24a3",
"#79352c", "#521250", "#c79ed2", "#d6dd92", "#e33e52", "#b2be57", "#fa06ec",
"#1bb699", "#6b2e5f", "#64820f", "#1c271", "#21538e", "#89d534", "#d36647",
"#7fb411", "#0023b8", "#3b8c2a", "#986b53", "#f50422", "#983f7a", "#ea24a3",
"#79352c", "#521250", "#c79ed2", "#d6dd92", "#e33e52", "#b2be57", "#fa06ec",
"#1bb699", "#6b2e5f", "#64820f", "#1c271", "#9cb64a", "#996c48", "#9ab9b7",
"#06e052", "#e3a481", "#0eb621", "#fc458e", "#b2db15", "#aa226d", "#792ed8",
"#73872a", "#520d3a", "#cefcb8", "#a5b3d9", "#7d1d85", "#c4fd57", "#f1ae16",
"#8fe22a", "#ef6e3c", "#243eeb", "#1dc18", "#dd93fd", "#3f8473", "#e7dbce",
"#421f79", "#7a3d93", "#635f6d", "#93f2d7", "#9b5c2a", "#15b9ee", "#0f5997",
"#409188", "#911e20", "#1350ce", "#10e5b1", "#fff4d7", "#cb2582", "#ce00be",
"#32d5d6", "#17232", "#608572", "#c79bc2", "#00f87c", "#77772a", "#6995ba",
"#fc6b57", "#f07815", "#8fd883", "#060e27", "#96e591", "#21d52e", "#d00043",
"#b47162", "#1ec227", "#4f0f6f", "#1d1d58", "#947002", "#bde052", "#e08c56",
"#28fcfd", "#bb09b", "#36486a", "#d02e29", "#1ae6db", "#3e464c", "#a84a8f",
"#911e7e", "#3f16d9", "#0f525f", "#ac7c0a", "#b4c086", "#c9d730", "#30cc49",
"#3d6751", "#fb4c03", "#640fc1", "#62c03e", "#d3493a", "#88aa0b", "#406df9",
"#615af0", "#4be47", "#2a3434", "#4a543f", "#79bca0", "#a8b8d4", "#00efd4",
"#7ad236", "#7260d8", "#1deaa7", "#06f43a", "#823c59", "#e3d94c", "#dc1c06",
"#f53b2a", "#b46238", "#2dfff6", "#a82b89", "#1a8011", "#436a9f", "#1a806a",
"#4cf09d", "#c188a2", "#67eb4b", "#b308d3", "#fc7e41", "#af3101", "#ff065",
"#71b1f4", "#a2f8a5", "#e23dd0", "#d3486d", "#00f7f9", "#474893", "#3cec35",
"#1c65cb", "#5d1d0c", "#2d7d2a", "#ff3420", "#5cdd87", "#a259a4", "#e4ac44",
"#1bede6", "#8798a4", "#d7790f", "#b2c24f", "#de73c2", "#d70a9c", "#25b67",
"#88e9b8", "#c2b0e2", "#86e98f", "#ae90e2", "#1a806b", "#436a9e", "#0ec0ff",
"#f812b3", "#b17fc9", "#8d6c2f", "#d3277a", "#2ca1ae", "#9685eb", "#8a96c6",
"#dba2e6", "#76fc1b", "#608fa4", "#20f6ba", "#07d7f6", "#dce77a", "#77ecca"]
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % moreColors.length);
    return moreColors[index];
}
window.onload = onNotificationReceived;
window.onload = connect;
messageForm.addEventListener('submit', sendMessage, true)

function chatBar() {
    var x = document.getElementById("chats");
    if (x.className === "chatbar") {
        x.className += " responsive";
    } else {
        x.className = "chatbar";
    }
}
