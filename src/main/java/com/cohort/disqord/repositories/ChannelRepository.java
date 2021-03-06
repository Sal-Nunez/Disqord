package com.cohort.disqord.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.cohort.disqord.models.Channel;

@Repository
public interface ChannelRepository extends CrudRepository<Channel, Long>{

	List<Channel> findAll();
	
}
