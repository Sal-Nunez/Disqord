package com.cohort.disqord.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.cohort.disqord.models.Category;
import com.cohort.disqord.models.Server;
import com.cohort.disqord.models.User;
import com.cohort.disqord.services.CategoryService;
import com.cohort.disqord.services.ChannelService;
import com.cohort.disqord.services.ServerService;
import com.cohort.disqord.services.UserService;

@Controller
public class CategoryController {

    @Autowired
    ChannelService channelServ;
    @Autowired
    CategoryService categoryServ;
    @Autowired
    ServerService serverServ;
    @Autowired
    UserService userService;


    @GetMapping("/servers/{server_id}/categories/new")
    public String newCategory(@ModelAttribute("category") Category category,
    		@PathVariable("server_id")Long server_id, HttpSession session, Model model) {
        if(session.getAttribute("uuid") == null) {
        return "redirect:/";
        } else {
            Long id = (Long) session.getAttribute("uuid");
            User user = userService.findById(id);
            model.addAttribute("user", user);
            Server server = serverServ.findById(server_id);
            model.addAttribute("server", server);
            return "newCategory.jsp";        	
        }
    }

    @PostMapping("/newCategory")
    public String createCategory(
            @Valid @ModelAttribute("category") Category category,
            BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return "newCategory.jsp";
        } else {
        	categoryServ.updateCreate(category);
            return "redirect:/dashboard";
        }
    }


    
}