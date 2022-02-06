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

import com.cohort.disqord.models.Category;
import com.cohort.disqord.models.Channel;
import com.cohort.disqord.models.ChannelMessage;
import com.cohort.disqord.models.Server;
import com.cohort.disqord.models.User;
import com.cohort.disqord.services.CategoryService;
import com.cohort.disqord.services.ChannelMessageService;
import com.cohort.disqord.services.ChannelService;
import com.cohort.disqord.services.ServerService;
import com.cohort.disqord.services.UserService;

@Controller
public class ChannelController {

    @Autowired
    ChannelService channelServ;

    @Autowired
    CategoryService categoryServ;

    @Autowired
    ServerService serverServ;

    @Autowired
    UserService userService;
    
    @Autowired
    ChannelMessageService channelMessageServ;


    @GetMapping("/servers/{server_id}/channels/new")
    public String newChannel(@ModelAttribute("channel") Channel channel,
    		@PathVariable("server_id")Long server_id, HttpSession session, Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
            Long id = (Long) session.getAttribute("uuid");
            User user = userService.findById(id);
            model.addAttribute("user", user);
// Server id for assignment ============
            Server server = serverServ.findById(server_id);
            model.addAttribute("server", server);
            
// Get all categories for possible assignment ==========
            List<Category> categories = categoryServ.findAll();
            model.addAttribute("categories", categories);
            return "newChannel.jsp";        	
        }
    }

    @PostMapping("/servers/{server_id}/newChannel")
    public String createChannel(
            @Valid @ModelAttribute("channel") Channel channel,
            BindingResult result, HttpSession session, Model model, 
            @PathVariable("server_id") Long server_id) {
        if (result.hasErrors()) {
        	// Server id for assignment ============
            Server server = serverServ.findById(server_id);
            model.addAttribute("server", server);
            // Get all categories for possible assignment ==========
            List<Category> categories = categoryServ.findAll();
            model.addAttribute("categories", categories);
            
            return "newChannel.jsp";
        } else {
            channelServ.save(channel);
            return "redirect:/servers/" + server_id;
        }
    }

    @GetMapping("/servers/{server_id}/channels/{channel_id}/edit")
    public String editsChannel(
            @ModelAttribute("channel") Channel channel,
            BindingResult result, HttpSession session,
            @PathVariable("channel_id") Long channel_id,
            @PathVariable("server_id") Long server_id,
            Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
            Long user_id = (Long) session.getAttribute("uuid");
            User user = userService.findById(user_id);
            model.addAttribute("user", user);
        	// Server id for assignment ============
            Server server = serverServ.findById(server_id);
            model.addAttribute("server", server);
            // Channel Id for assignment =========
            Channel thisChannel = channelServ.findById(channel_id);
            model.addAttribute("thisChannel", thisChannel);
            // Get all categories for possible assignment ==========
            List<Category> categories = categoryServ.findAll();
            model.addAttribute("categories", categories);
            return "editChannel.jsp";
        }
    }


    @PostMapping("/servers/{server_id}/channels/{channel_id}/update")
    public String editChannel(
            @Valid @ModelAttribute("channel") Channel channel,
            BindingResult result, HttpSession session,
            @PathVariable("server_id") Long server_id, 
            @PathVariable("channel_id")Long channel_id, 
            Model model) {
        if (result.hasErrors()) {
            Long user_id = (Long) session.getAttribute("uuid");
            User user = userService.findById(user_id);
            model.addAttribute("user", user);
        	// Server id for assignment ============
            Server server = serverServ.findById(server_id);
            model.addAttribute("server", server);
            // Channel Id for assignment =========
            Channel channelLoad = channelServ.findById(channel_id);
            model.addAttribute("channelLoad", channelLoad);
            // Get all categories for possible assignment ==========
            List<Category> categories = categoryServ.findAll();
            model.addAttribute("categories", categories);
            return "editChannel.jsp";
        } else {
            channelServ.save(channel);
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/servers/{server_id}/channels/{id}")
    public String channel(HttpSession session, @PathVariable("server_id")Long server_id,
            @PathVariable("id") Long channel_id,
            Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
        Long user_id = (Long) session.getAttribute("uuid");
        User user = userService.findById(user_id);
        model.addAttribute("user", user);
        Channel channel = channelServ.findById(channel_id);
        model.addAttribute("channel", channel);
        Server server = serverServ.findById(server_id);
        model.addAttribute("server", server);
        return "channel.jsp";
        }
    }

    @DeleteMapping("/channels/{id}/delete")
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
    
    @MessageMapping("/chat.sendMessage/channel/{id}")
    @SendTo("/topic/public/channel/{id}")
    public ChannelMessage sendMessage(@Payload ChannelMessage channelMessage) {
    	System.out.println("Channel");
    	channelMessageServ.save(channelMessage);
        return channelMessage;
    }
        
        
        
      
        
        
        
}
