package com.cohort.disqord.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cohort.disqord.models.ServerMember;
import com.cohort.disqord.repositories.ServerMemberRepository;

@Service
public class ServerMemberService {

	
	@Autowired
	ServerMemberRepository serverMemberRepo;
	
	public List<ServerMember> findAll() {
		return serverMemberRepo.findAll();
	}
	
	public ServerMember findById(Long id) {
		Optional<ServerMember> serverMember = serverMemberRepo.findById(id);
		if (serverMember.isPresent()) {
			return serverMember.get();
		} else {
			return null;
		}
	}
	
	public ServerMember save(ServerMember serverMember) {
		return serverMemberRepo.save(serverMember);
	}
	
	public void delete(Long id) {
		serverMemberRepo.deleteById(id);
	}

}
