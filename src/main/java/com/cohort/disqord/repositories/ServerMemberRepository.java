package com.cohort.disqord.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cohort.disqord.models.ServerMember;

public interface ServerMemberRepository extends CrudRepository<ServerMember, Long> {

	List<ServerMember> findAll();
	
}
