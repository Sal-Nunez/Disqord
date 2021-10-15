package com.cohort.disqord.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cohort.disqord.models.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	List<Role> findAll();
	
}
