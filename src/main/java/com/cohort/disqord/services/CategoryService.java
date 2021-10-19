package com.cohort.disqord.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cohort.disqord.models.Category;
import com.cohort.disqord.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	CategoryRepository categoryRepo;
	
	public List<Category> findAll() {
		return categoryRepo.findAll();
	}
	
	public Category findById(Long id) {
		Optional<Category> category = categoryRepo.findById(id);
		if (category.isPresent()) {
			return category.get();
		} else {
			return null;
		}
	}
	
	public Category updateCreate(Category category) {
		return categoryRepo.save(category);
	}
	
	public void delete(Long id) {
		categoryRepo.deleteById(id);
	}
	
	
}
