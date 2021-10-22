package com.cohort.disqord.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cohort.disqord.models.ChatMessage;
import com.cohort.disqord.repositories.ChatMessageRepository;

@Service
public class ChatMessageService {

	
	@Autowired
	ChatMessageRepository chatMessageRepo;
	
	public List<ChatMessage> findAll() {
		return chatMessageRepo.findAll();
	}
	
	public ChatMessage findById(Long id) {
		Optional<ChatMessage> chatMessage = chatMessageRepo.findById(id);
		if (chatMessage.isPresent()) {
			return chatMessage.get();
		} else {
			return null;
		}
	}
	
	public ChatMessage save(ChatMessage chatMessage) {
		return chatMessageRepo.save(chatMessage);
	}
	
	public void delete(Long id) {
		chatMessageRepo.deleteById(id);
	}

}