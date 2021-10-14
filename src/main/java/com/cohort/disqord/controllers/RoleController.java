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

import com.cohort.disqord.models.Role;
import com.cohort.disqord.models.User;
import com.cohort.disqord.services.RoleService;
import com.cohort.disqord.services.UserService;

@Controller
public class RoleController {

    @Autowired
    RoleService roleServ;

    @Autowired
    UserService userService;



    @GetMapping("/roles/new")
    public String newRole(@ModelAttribute("role") Role role, HttpSession session, Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
            Long id = (Long) session.getAttribute("uuid");
            User user = userService.findById(id);
            model.addAttribute("user", user);
            return "newRole.jsp";        	
        }
    }

    @PostMapping("/newRole")
    public String createRole(
            @Valid @ModelAttribute("role") Role role,
            BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return "newRole.jsp";
        } else {
            roleServ.updateCreate(role);
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/roles/{id}/edit")
    public String editsRole(
            @ModelAttribute("role") Role role,
            BindingResult result, HttpSession session,
            @PathVariable("id") Long id,
            Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
            Long user_id = (Long) session.getAttribute("uuid");
            User user = userService.findById(user_id);
            model.addAttribute("user", user);
            return "editRole.jsp";
        }
    }


    @PutMapping("/role/{id}/edit")
    public String editRole(
            @Valid @ModelAttribute("role") Role role,
            BindingResult result, HttpSession session, Model model) {
        if (result.hasErrors()) {
            Long user_id = (Long) session.getAttribute("uuid");
            User user = userService.findById(user_id);
            model.addAttribute("user", user);
            return "editRole.jsp";
        } else {
            roleServ.updateCreate(role);
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/roles/{id}")
    public String role(HttpSession session,
            @PathVariable("id") Long role_id,
            Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
        Long user_id = (Long) session.getAttribute("uuid");
        User user = userService.findById(user_id);
        Role role = roleServ.findById(role_id);
        model.addAttribute("role", role);
        model.addAttribute("user", user);
        return "role.jsp";
        }
    }

        @DeleteMapping("/roles/{id}")
        public String deleteRole(@PathVariable("id") Long role_id, HttpSession session) {
            Long id = (Long) session.getAttribute("uuid");
            User user = userService.findById(id);
            Role role = roleServ.findById(role_id);
            if (role.getServer().getOwner() == user) {
                roleServ.delete(role_id);
                return "redirect:/dashboard";
            }
            session.removeAttribute("uuid");
            return "redirect:/";
        }
}
