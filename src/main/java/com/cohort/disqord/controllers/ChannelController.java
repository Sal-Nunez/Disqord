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

import com.cohort.disqord.models.Channel;
import com.cohort.disqord.models.User;
import com.cohort.disqord.services.ChannelService;
import com.cohort.disqord.services.UserService;

@Controller
public class ChannelController {

    @Autowired
    ChannelService channelServ;

    @Autowired
    UserService userService;


    @GetMapping("/channels/new")
    public String newChannel(@ModelAttribute("channel") Channel channel, HttpSession session, Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
            Long id = (Long) session.getAttribute("uuid");
            User user = userService.findById(id);
            model.addAttribute("user", user);
            return "newChannel.jsp";        	
        }
    }

    @PostMapping("/newChannel")
    public String createChannel(
            @Valid @ModelAttribute("channel") Channel channel,
            BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return "newChannel.jsp";
        } else {
            channelServ.updateCreate(channel);
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/channels/{id}/edit")
    public String editsChannel(
            @ModelAttribute("channel") Channel channel,
            BindingResult result, HttpSession session,
            @PathVariable("id") Long id,
            Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
            Long user_id = (Long) session.getAttribute("uuid");
            User user = userService.findById(user_id);
            model.addAttribute("user", user);
            return "editChannel.jsp";
        }
    }


    @PutMapping("/channel/{id}/edit")
    public String editChannel(
            @Valid @ModelAttribute("channel") Channel channel,
            BindingResult result, HttpSession session, Model model) {
        if (result.hasErrors()) {
            Long user_id = (Long) session.getAttribute("uuid");
            User user = userService.findById(user_id);
            model.addAttribute("user", user);
            return "editChannel.jsp";
        } else {
            channelServ.updateCreate(channel);
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/channels/{id}")
    public String channel(HttpSession session,
            @PathVariable("id") Long channel_id,
            Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
        Long user_id = (Long) session.getAttribute("uuid");
        User user = userService.findById(user_id);
        Channel channel = channelServ.findById(channel_id);
        model.addAttribute("channel", channel);
        model.addAttribute("user", user);
        return "channel.jsp";
        }
    }

        @DeleteMapping("/channels/{id}")
        public String deleteChannel(@PathVariable("id") Long channel_id, HttpSession session) {
            Long id = (Long) session.getAttribute("uuid");
            User user = userService.findById(id);
            Channel channel = channelServ.findById(channel_id);
            if(channel.getServer().getOwner() == user) {
                channelServ.delete(channel_id);
                return "redirect:/dashboard";
                } else {
                    return "redirect:/";
                }
        }
}
