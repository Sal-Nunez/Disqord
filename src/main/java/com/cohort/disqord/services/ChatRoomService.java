package com.cohort.disqord.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cohort.disqord.models.ChatRoom;
import com.cohort.disqord.repositories.ChatRoomRepository;

@Service
public class ChatRoomService {

	
	@Autowired
	ChatRoomRepository chatRoomRepo;
	
	public List<ChatRoom> findAll() {
		return chatRoomRepo.findAll();
	}
	
	public ChatRoom findById(Long id) {
		Optional<ChatRoom> chatRoom = chatRoomRepo.findById(id);
		if (chatRoom.isPresent()) {
			return chatRoom.get();
		} else {
			return null;
		}
	}
	
	public ChatRoom updateCreate(ChatRoom chatRoom) {
		return chatRoomRepo.save(chatRoom);
	}
	
	public void delete(Long id) {
		chatRoomRepo.deleteById(id);
	}

}
