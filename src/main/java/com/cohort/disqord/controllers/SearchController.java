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

import com.cohort.disqord.models.ChatRoomNotification;
import com.cohort.disqord.models.User;
import com.cohort.disqord.models.ajaxSearch.AjaxResponseBody;
import com.cohort.disqord.models.ajaxSearch.SearchCriteria;
import com.cohort.disqord.services.ChatRoomNotificationServ;
import com.cohort.disqord.models.ChatRoom;
import com.cohort.disqord.models.Server;
import com.cohort.disqord.models.ServerMember;
import com.cohort.disqord.services.ChatRoomService;
import com.cohort.disqord.services.ServerService;
import com.cohort.disqord.services.UserService;

@RestController
public class SearchController {
	@Autowired
	UserService userServ;
	@Autowired
	ChatRoomService chatRoomServ;
	@Autowired
	ServerService serverServ;
	
	@Autowired
	ChatRoomNotificationServ chatRoomNotificationServ;
	
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
	
// Find all friends already in server
	@GetMapping("/friends/servers/{serverId}/serverMembers")
	public ResponseEntity<?> getFriendsInServerAjax(HttpSession session, @PathVariable("serverId") Long serverId){
		AjaxResponseBody result = new AjaxResponseBody();
		
		Server server = serverServ.findById(serverId);
		List<ServerMember> serMems = server.getServerMembers();
		
		// Change server members to users servermember.servermember
		ArrayList<User> users = new ArrayList<User>();
		for(ServerMember sm : serMems) {
			System.out.println(sm.getServerMember().getFirstName());
			System.out.println(sm.getServer().getName());
			users.add(sm.getServerMember());
		}
		
		// Filter out active user
		ArrayList<User> filteredUsers = new ArrayList<User>();
		for(User user : users) {
			if(user.getId() != (long)session.getAttribute("uuid")) {
				if((long) user.getId() != (long) server.getOwner().getId()) {
					filteredUsers.add(user);					
				}
			}
		}
		
		result.setResult(filteredUsers);			
		
		return ResponseEntity.ok(result);
	}
	
// Find all friends not in server
	@GetMapping("/friends/servers/{serverId}")
	public ResponseEntity<?> getFriendsNotInServerAjax(HttpSession session, @PathVariable("serverId") Long serverId){
		AjaxResponseBody result = new AjaxResponseBody();
		
		User user = userServ.findById((long)session.getAttribute("uuid"));
		List<User> friends = user.getFriends();
		
		Server server = serverServ.findById(serverId);
		List<ServerMember> serMems = server.getServerMembers();
		// Change server members to users servermember.servermember
		ArrayList<User> users = new ArrayList<User>();
		for(ServerMember sm : serMems) {
			users.add(sm.getServerMember());
		}
		
		friends.removeAll(users);
		
		result.setResult(friends);
		return ResponseEntity.ok(result);
	}
	
	
	@GetMapping("/chat_room_notifications/{chat_room_id}/{user_id}")
	public ChatRoomNotification chatRoomNotifications(@PathVariable("chat_room_id") Long chat_room_id, @PathVariable("user_id") Long user_id) {
		ChatRoomNotification cRN = chatRoomNotificationServ.findByUserIdAndChatRoomId(user_id, chat_room_id);
		if (cRN != null) {  			
		} else {
			ChatRoomNotification newCRN = new ChatRoomNotification();
			newCRN.setCount((long) 0);
			newCRN.setUser_id(user_id);
			newCRN.setChat_room_id(chat_room_id);
			chatRoomNotificationServ.save(newCRN);
		}
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
