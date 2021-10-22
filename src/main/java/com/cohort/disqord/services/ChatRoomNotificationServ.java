package com.cohort.disqord.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cohort.disqord.models.ChatRoomNotification;
import com.cohort.disqord.repositories.ChatRoomNotificationRepo;

@Service
public class ChatRoomNotificationServ {

	
	@Autowired
	ChatRoomNotificationRepo chatRoomNotificationRepo;
	
	
	public ChatRoomNotification findById(Long id) {
		Optional<ChatRoomNotification> chatRoomNotification = chatRoomNotificationRepo.findById(id);
		if (chatRoomNotification.isPresent()) {
			return chatRoomNotification.get();
		} else {
			return null;
		}
	}
	
	public ChatRoomNotification save(ChatRoomNotification chatRoomNotification) {
		return chatRoomNotificationRepo.save(chatRoomNotification);
	}
	
	public void delete(Long id) {
		chatRoomNotificationRepo.deleteById(id);
	}

	public ChatRoomNotification findByUserIdAndChatRoomId(Long one, Long two) {
		return chatRoomNotificationRepo.getChatRoomNotificationWhereUserIdAndChatRoomId(one, two);
		
	}
	
	
	
	
}