package com.cohort.disqord.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cohort.disqord.models.User;
import com.cohort.disqord.models.ajaxSearch.AjaxResponseBody;
import com.cohort.disqord.models.ajaxSearch.SearchCriteria;
import com.cohort.disqord.services.UserService;

@RestController
public class SearchController {
	@Autowired
	UserService userServ;
	

	
	@PostMapping("/friends/search")
	public ResponseEntity<?> getSearchResultViaAjax(@Valid @RequestBody SearchCriteria search, Errors errors){
		
		// result is just the jsonified message printed 
		AjaxResponseBody result = new AjaxResponseBody();
		
		// Return 404 w error
		if(errors.hasErrors()) {
			result.setMsg(errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}
		// Where the magic happens
		List<User> users = userServ.ajaxSearch(search.getInput()+"%");
		System.out.println(users);
		
		// None found
		if(users.isEmpty()) {
			result.setMsg("No user found.");
		} else {
			result.setMsg("success");
		}
		result.setResult(users);
		
		return ResponseEntity.ok(result);
	}}
