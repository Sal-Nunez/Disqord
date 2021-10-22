package com.cohort.disqord.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cohort.disqord.models.ChannelMessage;
import com.cohort.disqord.repositories.ChannelMessageRepository;

@Service
public class ChannelMessageService {

	
	@Autowired
	ChannelMessageRepository channelMessageRepo;
	
	public List<ChannelMessage> findAll() {
		return channelMessageRepo.findAll();
	}
	
	public ChannelMessage findById(Long id) {
		Optional<ChannelMessage> channelMessage = channelMessageRepo.findById(id);
		if (channelMessage.isPresent()) {
			return channelMessage.get();
		} else {
			return null;
		}
	}
	
	public ChannelMessage save(ChannelMessage channelMessage) {
		return channelMessageRepo.save(channelMessage);
	}
	
	public void delete(Long id) {
		channelMessageRepo.deleteById(id);
	}

}
