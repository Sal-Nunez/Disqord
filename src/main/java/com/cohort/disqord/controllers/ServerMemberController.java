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

import com.cohort.disqord.models.ServerMember;
import com.cohort.disqord.models.User;
import com.cohort.disqord.services.ServerMemberService;
import com.cohort.disqord.services.UserService;

@Controller
public class ServerMemberController {

    @Autowired
    ServerMemberService serverMemberServ;

    @Autowired
    UserService userService;



    @GetMapping("/serverMembers/new")
    public String newServerMember(@ModelAttribute("serverMember") ServerMember serverMember, HttpSession session, Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
            Long id = (Long) session.getAttribute("uuid");
            User user = userService.findById(id);
            model.addAttribute("user", user);
            return "newServerMember.jsp";        	
        }
    }

    @PostMapping("/newServerMember")
    public String createServerMember(
            @Valid @ModelAttribute("serverMember") ServerMember serverMember,
            BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return "newServerMember.jsp";
        } else {
            serverMemberServ.save(serverMember);
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/serverMembers/{id}/edit")
    public String editsServerMember(
            @ModelAttribute("serverMember") ServerMember serverMember,
            BindingResult result, HttpSession session,
            @PathVariable("id") Long id,
            Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
            Long user_id = (Long) session.getAttribute("uuid");
            User user = userService.findById(user_id);
            model.addAttribute("user", user);
            return "editServerMember.jsp";
        }
    }


    @PutMapping("/serverMember/{id}/edit")
    public String editServerMember(
            @Valid @ModelAttribute("serverMember") ServerMember serverMember,
            BindingResult result, HttpSession session, Model model) {
        if (result.hasErrors()) {
            Long user_id = (Long) session.getAttribute("uuid");
            User user = userService.findById(user_id);
            model.addAttribute("user", user);
            return "editServerMember.jsp";
        } else {
            serverMemberServ.save(serverMember);
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/serverMembers/{id}")
    public String serverMember(HttpSession session,
            @PathVariable("id") Long serverMember_id,
            Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
        Long user_id = (Long) session.getAttribute("uuid");
        User user = userService.findById(user_id);
        ServerMember serverMember = serverMemberServ.findById(serverMember_id);
        model.addAttribute("serverMember", serverMember);
        model.addAttribute("user", user);
        return "serverMember.jsp";
        }
    }
    
        @DeleteMapping("/serverMembers/{id}")
        public String deleteServerMember(@PathVariable("id") Long serverMember_id, HttpSession session) {
            Long id = (Long) session.getAttribute("uuid");
            User user = userService.findById(id);
            ServerMember serverMember = serverMemberServ.findById(serverMember_id);
            if (serverMember.getServer().getOwner() == user || serverMember.getServerMember() == user) {
                serverMemberServ.delete(serverMember_id);
                return "redirect:/dashboard";
            }
            session.removeAttribute("uuid");
            return "redirect:/";
        }
        
}
