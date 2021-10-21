package com.cohort.disqord.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cohort.disqord.models.ServerMember;

public interface ServerMemberRepository extends CrudRepository<ServerMember, Long> {

	List<ServerMember> findAll();
	
	@Query(value="SELECT * FROM server_members WHERE member_id = ?1 AND server_id = ?2",
            nativeQuery=true)
    ServerMember  getServerMemberByUserIdAndServerId(Long userId, Long chatRoomId);
}
