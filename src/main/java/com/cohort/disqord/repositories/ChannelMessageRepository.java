package com.cohort.disqord.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cohort.disqord.models.ChannelMessage;



public interface ChannelMessageRepository extends CrudRepository<ChannelMessage, Long> {

	List<ChannelMessage> findAll();
	
}
