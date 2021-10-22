package com.cohort.disqord.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cohort.disqord.models.Role;
import com.cohort.disqord.repositories.RoleRepository;

@Service
public class RoleService {

	
	@Autowired
	RoleRepository roleRepo;
	
	public List<Role> findAll() {
		return roleRepo.findAll();
	}
	
	public Role findById(Long id) {
		Optional<Role> role = roleRepo.findById(id);
		if (role.isPresent()) {
			return role.get();
		} else {
			return null;
		}
	}
	
	public Role save(Role role) {
		return roleRepo.save(role);
	}
	
	public void delete(Long id) {
		roleRepo.deleteById(id);
	}

}
