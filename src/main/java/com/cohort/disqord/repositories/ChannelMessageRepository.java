package com.cohort.disqord.repositories;

import org.springframework.data.repository.CrudRepository;

import com.cohort.disqord.models.ChannelMessage;

public interface ChannelMessageRepository extends CrudRepository<ChannelMessage, Long> {

}
