package com.cohort.disqord.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cohort.disqord.models.Server;
import com.cohort.disqord.models.ServerMember;
import com.cohort.disqord.repositories.ServerRepository;

@Service
public class ServerService {
	
	@Autowired
	ServerRepository serverRepo;
	
	public List<Server> findAll() {
		return serverRepo.findAll();
	}
	
	public Server findById(Long id) {
		Optional<Server> server = serverRepo.findById(id);
		if (server.isPresent()) {
			return server.get();
		} else {
			return null;
		}
	}
	
	public Server save(Server server) {
		return serverRepo.save(server);
	}
	
	public void delete(Long id) {
		serverRepo.deleteById(id);
	}
	
	public void addUser(Long serverId, ServerMember servMem) {
		Server server = findById(serverId);
		server.getServerMembers().add(servMem);
		serverRepo.save(server);
	}
	
	public void removeUser(Server server, ServerMember servMem) {
		server.getServerMembers().remove(servMem);
		serverRepo.save(server);
	}

	
}
