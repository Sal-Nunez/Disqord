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

import com.cohort.disqord.models.ChannelMessage;
import com.cohort.disqord.models.User;
import com.cohort.disqord.services.ChannelMessageService;
import com.cohort.disqord.services.UserService;

@Controller
public class ChannelMessageController {

    @Autowired
    ChannelMessageService channelMessageServ;

    @Autowired
    UserService userService;


    @GetMapping("/channelMessages/new")
    public String newChannelMessage(@ModelAttribute("channelMessage") ChannelMessage channelMessage, HttpSession session, Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
            Long id = (Long) session.getAttribute("uuid");
            User user = userService.findById(id);
            model.addAttribute("user", user);
            return "newChannelMessage.jsp";        	
        }
    }

    @PostMapping("/newChannelMessage")
    public String createChannelMessage(
            @Valid @ModelAttribute("channelMessage") ChannelMessage channelMessage,
            BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return "newChannelMessage.jsp";
        } else {
            channelMessageServ.updateCreate(channelMessage);
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/channelMessages/{id}/edit")
    public String editsChannelMessage(
            @ModelAttribute("channelMessage") ChannelMessage channelMessage,
            BindingResult result, HttpSession session,
            @PathVariable("id") Long id,
            Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
            Long user_id = (Long) session.getAttribute("uuid");
            User user = userService.findById(user_id);
            model.addAttribute("user", user);
            return "editChannelMessage.jsp";
        }
    }


    @PutMapping("/channelMessage/{id}/edit")
    public String editChannelMessage(
            @Valid @ModelAttribute("channelMessage") ChannelMessage channelMessage,
            BindingResult result, HttpSession session, Model model) {
        if (result.hasErrors()) {
            Long user_id = (Long) session.getAttribute("uuid");
            User user = userService.findById(user_id);
            model.addAttribute("user", user);
            return "editChannelMessage.jsp";
        } else {
            channelMessageServ.updateCreate(channelMessage);
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/channelMessages/{id}")
    public String channelMessage(HttpSession session,
            @PathVariable("id") Long channelMessage_id,
            Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
        Long user_id = (Long) session.getAttribute("uuid");
        User user = userService.findById(user_id);
        ChannelMessage channelMessage = channelMessageServ.findById(channelMessage_id);
        model.addAttribute("channelMessage", channelMessage);
        model.addAttribute("user", user);
        return "channelMessage.jsp";
        }
    }

        @DeleteMapping("/channelMessages/{id}")
        public String deleteChannelMessage(@PathVariable("id") Long channelMessage_id, HttpSession session) {
            Long id = (Long) session.getAttribute("uuid");
            User user = userService.findById(id);
            ChannelMessage channelMessage = channelMessageServ.findById(channelMessage_id);
            if (channelMessage.getUser() == user) {
                channelMessageServ.delete(channelMessage_id);
                return "redirect:/dashboard";
            }
            session.removeAttribute("uuid");
            return "redirect:/";
        }
}
