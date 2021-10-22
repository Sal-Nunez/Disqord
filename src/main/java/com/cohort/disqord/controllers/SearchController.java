package com.cohort.disqord.controllers;

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

import com.cohort.disqord.models.ChatRoomNotification;
import com.cohort.disqord.models.User;
import com.cohort.disqord.models.ajaxSearch.AjaxResponseBody;
import com.cohort.disqord.models.ajaxSearch.SearchCriteria;
import com.cohort.disqord.services.ChatRoomNotificationServ;
import com.cohort.disqord.services.UserService;

@RestController
public class SearchController {
	@Autowired
	UserService userServ;
	
	@Autowired
	ChatRoomNotificationServ chatRoomNotificationServ;
	
//	AJAX ROUTES

//	Ajax search for friends
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

	
	@GetMapping("/chat_room_notifications/{chat_room_id}/{user_id}")
	public ChatRoomNotification chatRoomNotifications(@PathVariable("chat_room_id") Long chat_room_id, @PathVariable("user_id") Long user_id) {
		ChatRoomNotification cRN = chatRoomNotificationServ.findByUserIdAndChatRoomId(user_id, chat_room_id);
		return cRN;
	}
	
	
	@GetMapping("/chat_room_notifications/{chat_room_id}/{user_id}/reset")
	public ChatRoomNotification deleteChatRoomNotifications(@PathVariable("chat_room_id") Long chat_room_id, @PathVariable("user_id") Long user_id) {
		ChatRoomNotification cRN = chatRoomNotificationServ.findByUserIdAndChatRoomId(user_id, chat_room_id);
		cRN.setCount((long) 0);
		chatRoomNotificationServ.save(cRN);
		return cRN;
	}
	
	
}
