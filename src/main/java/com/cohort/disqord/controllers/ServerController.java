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
import com.cohort.disqord.models.User;
import com.cohort.disqord.services.ServerService;
import com.cohort.disqord.services.UserService;

@Controller
public class ServerController {

    @Autowired
    ServerService serverServ;

    @Autowired
    UserService userService;


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
        if (result.hasErrors()) {
            return "newServer.jsp";
        } else {
            serverServ.updateCreate(server);
            return "redirect:/dashboard";
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
    public String server(HttpSession session,
            @PathVariable("id") Long server_id,
            Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
        Long user_id = (Long) session.getAttribute("uuid");
        User user = userService.findById(user_id);
        Server server = serverServ.findById(server_id);
        model.addAttribute("server", server);
        model.addAttribute("user", user);
        return "server.jsp";
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
}
