package com.cohort.disqord.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cohort.disqord.models.Server;

public interface ServerRepository extends CrudRepository<Server, Long> {

	List<Server> findAll();
	
}
