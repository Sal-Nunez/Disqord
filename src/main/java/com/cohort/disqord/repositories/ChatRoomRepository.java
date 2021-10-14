package com.cohort.disqord.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cohort.disqord.models.ChatRoom;

public interface ChatRoomRepository extends CrudRepository<ChatRoom, Long> {

	List<ChatRoom> findAll();
	
}
