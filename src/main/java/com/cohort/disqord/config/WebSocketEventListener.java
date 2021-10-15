package com.cohort.disqord.config;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.cohort.disqord.models.ChatMessage;
import com.cohort.disqord.models.User;
import com.cohort.disqord.services.UserService;

@Component
public class WebSocketEventListener {
	
	@Autowired
	UserService userServ;
	
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event, HttpSession session) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        
        Long userId = (Long) session.getAttribute("uuid");  
        User user = userServ.getOne(userId);
        
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setUser(user);
        
        messagingTemplate.convertAndSend("/topic/public", chatMessage);
    }

}
