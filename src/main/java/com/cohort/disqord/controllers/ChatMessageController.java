package com.cohort.disqord.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.cohort.disqord.models.User;
import com.cohort.disqord.services.ChatMessageService;
import com.cohort.disqord.services.UserService;

@Controller
public class ChatMessageController {

    @Autowired
    ChatMessageService chatMessageServ;

    @Autowired
    UserService userService;


    @GetMapping("/chatMessages/new")
    public String newChatMessage(@ModelAttribute("chatMessage") ChatMessage chatMessage, HttpSession session, Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
            Long id = (Long) session.getAttribute("uuid");
            User user = userService.findById(id);
            model.addAttribute("user", user);
            return "newChatMessage.jsp";        	
        }
    }

    @PostMapping("/newChatMessage")
    public String createChatMessage(
            @Valid @ModelAttribute("chatMessage") ChatMessage chatMessage,
            BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return "newChatMessage.jsp";
        } else {
            chatMessageServ.save(chatMessage);
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/chatMessages/{id}/edit")
    public String editsChatMessage(
            @ModelAttribute("chatMessage") ChatMessage chatMessage,
            BindingResult result, HttpSession session,
            @PathVariable("id") Long id,
            Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
            Long user_id = (Long) session.getAttribute("uuid");
            User user = userService.findById(user_id);
            model.addAttribute("user", user);
            return "editChatMessage.jsp";
        }
    }


    @PutMapping("/chatMessage/{id}/edit")
    public String editChatMessage(
            @Valid @ModelAttribute("chatMessage") ChatMessage chatMessage,
            BindingResult result, HttpSession session, Model model) {
        if (result.hasErrors()) {
            Long user_id = (Long) session.getAttribute("uuid");
            User user = userService.findById(user_id);
            model.addAttribute("user", user);
            return "editChatMessage.jsp";
        } else {
            chatMessageServ.save(chatMessage);
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/chatMessages/{id}")
    public String chatMessage(HttpSession session,
            @PathVariable("id") Long chatMessage_id,
            Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
        Long user_id = (Long) session.getAttribute("uuid");
        User user = userService.findById(user_id);
        ChatMessage chatMessage = chatMessageServ.findById(chatMessage_id);
        model.addAttribute("chatMessage", chatMessage);
        model.addAttribute("user", user);
        return "chatMessage.jsp";
        }
    }

        @DeleteMapping("/chatMessages/{id}")
        public String deleteChatMessage(@PathVariable("id") Long chatMessage_id, HttpSession session) {
            Long id = (Long) session.getAttribute("uuid");
            User user = userService.findById(id);
            ChatMessage chatMessage = chatMessageServ.findById(chatMessage_id);
            if (chatMessage.getUser() == user) {
                chatMessageServ.delete(chatMessage_id);
                return "redirect:/dashboard";
            }
            session.removeAttribute("uuid");
            return "redirect:/";
        }
}
