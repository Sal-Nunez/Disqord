package com.cohort.disqord.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cohort.disqord.models.Channel;
import com.cohort.disqord.repositories.ChannelRepository;

@Service
public class ChannelService {

	
	@Autowired
	ChannelRepository channelRepo;
	
	public List<Channel> findAll() {
		return channelRepo.findAll();
	}
	
	public Channel findById(Long id) {
		Optional<Channel> channel = channelRepo.findById(id);
		if (channel.isPresent()) {
			return channel.get();
		} else {
			return null;
		}
	}
	
	public Channel save(Channel channel) {
		return channelRepo.save(channel);
	}
	
	public void delete(Long id) {
		channelRepo.deleteById(id);
	}

}
