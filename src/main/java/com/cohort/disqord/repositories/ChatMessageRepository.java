package com.cohort.disqord.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cohort.disqord.models.ChatMessage;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {

	List<ChatMessage> findAll();
	
}
