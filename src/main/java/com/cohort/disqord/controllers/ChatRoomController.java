package com.cohort.disqord.controllers;



import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.cohort.disqord.models.ChatMessage;
import com.cohort.disqord.models.ChatRoom;
import com.cohort.disqord.models.ChatRoomNotification;
import com.cohort.disqord.models.User;
import com.cohort.disqord.services.ChatMessageService;
import com.cohort.disqord.services.ChatRoomNotificationServ;
import com.cohort.disqord.services.ChatRoomService;
import com.cohort.disqord.services.UserService;

@Controller
public class ChatRoomController {

    @Autowired
    ChatRoomService chatRoomServ;

    @Autowired
    UserService userService;
    
    @Autowired
    ChatMessageService chatMessageServ;
    
    @Autowired
    ChatRoomNotificationServ chatRoomNotificationServ;



    @GetMapping("/chatRooms/new")
    public String newChatRoom(@ModelAttribute("chatRoom") ChatRoom chatRoom, HttpSession session, Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
            Long id = (Long) session.getAttribute("uuid");
            User user = userService.findById(id);
            model.addAttribute("user", user);
            return "newChatRoom.jsp";        	
        }
    }

    @PostMapping("/newChatRoom")
    public String createChatRoom(
            @Valid @ModelAttribute("chatRoom") ChatRoom chatRoom,
            BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return "newChatRoom.jsp";
        } else {
        	User user = userService.findById((Long) session.getAttribute("uuid"));
        	chatRoom.setUser(user);
            chatRoomServ.save(chatRoom);
            return "redirect:/chatRoomMembers/add/" + chatRoom.getId();
        }
    }
    
    @GetMapping("/chatRoomMembers/add/{id}")
    public String addUserToChatRoom(@PathVariable("id") Long id, HttpSession session) {
    	User user = userService.findById((Long) session.getAttribute("uuid"));
    	chatRoomServ.addUser(id, user);
    	return "redirect:/chatRooms/" +id;
    }

    @GetMapping("/chatRooms/{id}/edit")
    public String editsChatRoom(
            @ModelAttribute("chatRoom") ChatRoom chatRoom,
            BindingResult result, HttpSession session,
            @PathVariable("id") Long id,
            Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
            Long user_id = (Long) session.getAttribute("uuid");
            User user = userService.findById(user_id);
            model.addAttribute("user", user);
            return "editChatRoom.jsp";
        }
    }


    @PutMapping("/chatRoom/{id}/edit")
    public String editChatRoom(
            @Valid @ModelAttribute("chatRoom") ChatRoom chatRoom,
            BindingResult result, HttpSession session, Model model) {
        if (result.hasErrors()) {
            Long user_id = (Long) session.getAttribute("uuid");
            User user = userService.findById(user_id);
            model.addAttribute("user", user);
            return "editChatRoom.jsp";
        } else {
            chatRoomServ.save(chatRoom);
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/chatRooms/{id}")
    public String chatRoom(HttpSession session,
            @PathVariable("id") Long chatRoom_id,
            Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
        Long user_id = (Long) session.getAttribute("uuid");
        User user = userService.findById(user_id);
        ChatRoom chatRoom = chatRoomServ.findById(chatRoom_id);
        model.addAttribute("chatRoom", chatRoom);
        model.addAttribute("user", user);
        return "chatRoom.jsp";
        }
    }

    @DeleteMapping("/chatRooms/{id}")
    public String deleteChatRoom(@PathVariable("id") Long chatRoom_id, HttpSession session) {
        Long id = (Long) session.getAttribute("uuid");
        User user = userService.findById(id);
        ChatRoom chatRoom = chatRoomServ.findById(chatRoom_id);
        if (chatRoom.getUser() == user) {
            chatRoomServ.delete(chatRoom_id);
            return "redirect:/dashboard";
        }
        session.removeAttribute("uuid");
        return "redirect:/";
    }	
	
    @MessageMapping("/chat.sendMessage/chat_room/{id}")
    @SendTo("/topic/public/chat_room/{id}")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, @PathVariable("id") String room) {
    	chatMessageServ.save(chatMessage);
        return chatMessage;
    }

    @MessageMapping("sendNotification/chat_room/{chat_room_id}")
    @SendTo("/topic/notification/chat_room/{chat_room_id}")
    public ChatRoomNotification sendNotification(@Payload ChatRoomNotification chatRoomNotification) {
		ChatRoomNotification cRN1 = chatRoomNotificationServ.findByUserIdAndChatRoomId(chatRoomNotification.getUser_id(), chatRoomNotification.getChat_room_id());
    	ChatRoom chatRoom = chatRoomServ.findById(chatRoomNotification.getChat_room_id());
    	List<User> cRM = chatRoom.getChatRoomMembers();
    	Long chat_room_id = chatRoomNotification.getChat_room_id();
    	for(User user : cRM) {
    		ChatRoomNotification cRN = chatRoomNotificationServ.findByUserIdAndChatRoomId(user.getId(), chat_room_id);
    		if (cRN != null) {
    			cRN.setCount(cRN.getCount() + 1);
    			chatRoomNotificationServ.save(cRN);    			
    		} else {
    			ChatRoomNotification newCRN = new ChatRoomNotification();
    			newCRN.setCount((long) 1);
    			newCRN.setUser_id(user.getId());
    			newCRN.setChat_room_id(chat_room_id);
    			chatRoomNotificationServ.save(newCRN);
    		}
    	}
    	return cRN1;    	
    }
    
    // Invite users to room
    @GetMapping("/friends/invite/{friendId}/room/{roomId}")
    public String inviteUserToRoom(@PathVariable("friendId") Long friendId, @PathVariable("roomId") Long roomId, HttpSession session) {
    	User invitee = userService.findById(friendId);
    	chatRoomServ.addUser(roomId, invitee);
    	return "redirect:/chatRooms/" + roomId;
    }
    
    // Kick users from room (delete relationship)
    @DeleteMapping("/friends/kick/{friendId}/room/{roomId}")
    public String kickUserFromRoom(@PathVariable("friendId") Long friendId, @PathVariable("roomId") Long roomId, HttpSession session) {
    	User kickee = userService.findById(friendId);
    	ChatRoom chatRoom = chatRoomServ.findById(roomId);
    	System.out.println(chatRoom.getUser().getId());
    	if((long)session.getAttribute("uuid") == chatRoom.getUser().getId()) {
    		chatRoomServ.removeUser(chatRoom, kickee);
    		return "redirect:/chatRooms/" + roomId;
    	} else {
    		return "redirect:/chatRooms/" + roomId;
    	}
    }
    


}

