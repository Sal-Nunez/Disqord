package com.cohort.disqord.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cohort.disqord.models.Channel;

public interface ChannelRepository extends CrudRepository<Channel, Long>{

	List<Channel> findAll();
	
}
