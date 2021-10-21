package com.cohort.disqord.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cohort.disqord.models.ChatRoom;
import com.cohort.disqord.models.User;
import com.cohort.disqord.models.ajaxSearch.AjaxResponseBody;
import com.cohort.disqord.models.ajaxSearch.SearchCriteria;
import com.cohort.disqord.services.ChatRoomService;
import com.cohort.disqord.services.UserService;

@RestController
public class SearchController {
	@Autowired
	UserService userServ;
	@Autowired
	ChatRoomService chatRoomServ;
	
//	AJAX ROUTES

//	Ajax search for friends
	@PostMapping("/friends/search")
	public ResponseEntity<?> getSearchResultViaAjax(@Valid @RequestBody SearchCriteria search, Errors errors, HttpSession session){
		
		// result is just the jsonified message printed 
		AjaxResponseBody result = new AjaxResponseBody();
		
		// Return 404 w error
		if(errors.hasErrors()) {
			result.setMsg(errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}
		
		// Where the magic happens - filter out active user
		List<User> users = userServ.ajaxSearch(search.getInput()+"%");
		ArrayList<User> filteredUsers = new ArrayList<User>();
		for(User user : users) {
			if(user.getId() != (long)session.getAttribute("uuid")) {
				filteredUsers.add(user);
			}
		}
		
		// None found
		if(users.isEmpty()) {
			result.setMsg("No user found.");
		} else {
			result.setMsg("success");
		}
		result.setResult(filteredUsers);
		
		return ResponseEntity.ok(result);
	}
	
//	Find all friends for My Friends page
	@GetMapping("/friends")
	public ResponseEntity<?> getFriendsViaAjax(HttpSession session){
		AjaxResponseBody result = new AjaxResponseBody();
		
		User user = userServ.findById((long)session.getAttribute("uuid"));
		List<User> friends = user.getFriends();
		
		result.setResult(friends);
		return ResponseEntity.ok(result);
	}
	
// Find all friends already in chat room
	@GetMapping("/friends/{roomId}/members")
	public ResponseEntity<?> getFriendsInRoomAjax(HttpSession session, @PathVariable("roomId") Long roomId){
		AjaxResponseBody result = new AjaxResponseBody();
		
		ChatRoom chatRoom = chatRoomServ.findById(roomId);
		List<User> users = chatRoom.getChatRoomMembers();
		
		// Filter out active user
		ArrayList<User> filteredUsers = new ArrayList<User>();
		for(User user : users) {
			System.out.println(chatRoom.getUser().getId());
			if(user.getId() != (long)session.getAttribute("uuid")) {
				if((long) user.getId() != (long) chatRoom.getUser().getId()) {
					filteredUsers.add(user);					
				}
			}
		}
			
		result.setResult(filteredUsers);			

		return ResponseEntity.ok(result);
	}
	
// Find all friends not in chat room
	@GetMapping("/friends/{roomId}")
	public ResponseEntity<?> getFriendsNotInRoomAjax(HttpSession session, @PathVariable("roomId") Long roomId){
		AjaxResponseBody result = new AjaxResponseBody();
		
		User user = userServ.findById((long)session.getAttribute("uuid"));
		List<User> friends = user.getFriends();
		
		ChatRoom chatRoom = chatRoomServ.findById(roomId);
		List<User> members = chatRoom.getChatRoomMembers();
		
		friends.removeAll(members);
		
		result.setResult(friends);
		return ResponseEntity.ok(result);
	}
	
	
}
