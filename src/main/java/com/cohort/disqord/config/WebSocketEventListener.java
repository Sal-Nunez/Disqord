package com.cohort.disqord.config;



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


@Component
public class WebSocketEventListener {
	
	
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String chat_room_id = (String) headerAccessor.getSessionAttributes().get("chat_room_id");
        String channel_id = (String) headerAccessor.getSessionAttributes().get("channel_id");
        System.out.println(channel_id);
        System.out.println(chat_room_id);
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null) {
            logger.info("User Disconnected : " + username);

            ChatMessage chatMessage = new ChatMessage();
            if (chat_room_id != null) {
            	messagingTemplate.convertAndSend("/topic/public/chat_room/" + chat_room_id, chatMessage);            	
            }
    }
    

}
}
