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

import com.cohort.disqord.models.Server;
import com.cohort.disqord.models.ServerMember;
import com.cohort.disqord.models.User;
import com.cohort.disqord.services.ChannelService;
import com.cohort.disqord.services.ServerMemberService;
import com.cohort.disqord.services.ServerService;
import com.cohort.disqord.services.UserService;

@Controller
public class ServerController {

    @Autowired
    ServerService serverServ;

    @Autowired
    UserService userService;

    @Autowired
    ChannelService channelServ;
    
    @Autowired
    ServerMemberService serverMemberServ;


    @GetMapping("/servers/new")
    public String newServer(@ModelAttribute("server") Server server, HttpSession session, Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
            Long id = (Long) session.getAttribute("uuid");
            User user = userService.findById(id);
            model.addAttribute("user", user);
            return "newServer.jsp";        	
        }
    }

    @PostMapping("/newServer")
    public String createServer(
            @Valid @ModelAttribute("server") Server server,
            BindingResult result, HttpSession session) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
	        if (result.hasErrors()) {
	            return "newServer.jsp";
	        } else {
	        	Long id = (Long) session.getAttribute("uuid");
	        	User user = userService.findById(id);
	        	server.setOwner(user);
	        	serverServ.updateCreate(server);
	        	// Save owner as server member
	        	ServerMember serverMember = new ServerMember();
	        	serverMember.setServerMember(user);
	        	serverMember.setServer(server);
	        	serverMemberServ.updateCreate(serverMember);
	            return "redirect:/dashboard";
	        }
        }
    }

    @GetMapping("/servers/{id}/edit")
    public String editsServer(
            @ModelAttribute("server") Server server,
            BindingResult result, HttpSession session,
            @PathVariable("id") Long id,
            Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
            Long user_id = (Long) session.getAttribute("uuid");
            User user = userService.findById(user_id);
            model.addAttribute("user", user);
            return "editServer.jsp";
        }
    }


    @PutMapping("/server/{id}/edit")
    public String editServer(
            @Valid @ModelAttribute("server") Server server,
            BindingResult result, HttpSession session, Model model) {
        if (result.hasErrors()) {
            Long user_id = (Long) session.getAttribute("uuid");
            User user = userService.findById(user_id);
            model.addAttribute("user", user);
            return "editServer.jsp";
        } else {
            serverServ.updateCreate(server);
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/servers/{id}")
    public String server(HttpSession session, @PathVariable("id") Long server_id,
            Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
        Long user_id = (Long) session.getAttribute("uuid");
        User user = userService.findById(user_id);
        Server server = serverServ.findById(server_id);
        model.addAttribute("server", server);
        model.addAttribute("user", user);
   
        System.out.println(server.getChannels());
        return "serverView.jsp";
        }
    }

    @DeleteMapping("/servers/{id}")
    public String deleteServer(@PathVariable("id") Long server_id, HttpSession session) {
        Long id = (Long) session.getAttribute("uuid");
        User user = userService.findById(id);
        Server server = serverServ.findById(server_id);
        if (server.getOwner() == user) {
            serverServ.delete(server_id);
            return "redirect:/dashboard";
        }
        session.removeAttribute("uuid");
        return "redirect:/";
    }
    
    // Instantiate invited user as server member then redirect to route below
    @GetMapping("/friends/invite/{friendId}/servers/{serverId}")
    public String inviteUserToServer(@PathVariable("friendId") Long friendId, @PathVariable("serverId") Long serverId, HttpSession session) {
    	User invitee = userService.findById(friendId);
    	System.out.println("in serveermember add");
    	ServerMember serverMember = new ServerMember();
    	serverMember.setServerMember(invitee);
    	serverMember.setServer(serverServ.findById(serverId));
    	serverMemberServ.updateCreate(serverMember);
    	return "redirect:/servers/"+ serverId;
    }
    
    // Kick users from room (delete relationship)
    @DeleteMapping("/friends/kick/{friendId}/servers/{serverId}")
    public String kickUserFromServer(@PathVariable("friendId") Long friendId, @PathVariable("serverId") Long serverId, HttpSession session) {
    	
    	ServerMember sm = serverMemberServ.findByUserIdAndServerId(friendId, serverId);
    	Server server = serverServ.findById(serverId);
    	
    	
    	if((long)session.getAttribute("uuid") == server.getOwner().getId()) {
    		serverMemberServ.delete(sm.getId());
    		return "redirect:/servers/" + serverId;
    	} else {
    		return "redirect:/servers/" + serverId;
    	}
    }
}
