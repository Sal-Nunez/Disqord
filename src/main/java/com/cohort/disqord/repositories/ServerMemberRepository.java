package com.cohort.disqord.repositories;

import org.springframework.data.repository.CrudRepository;

import com.cohort.disqord.models.ServerMember;

public interface ServerMemberRepository extends CrudRepository<ServerMember, Long> {

}
