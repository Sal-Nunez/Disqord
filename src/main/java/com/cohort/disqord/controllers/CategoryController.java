package com.cohort.disqord.controllers;

import java.util.List;

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
import com.cohort.disqord.models.Channel;
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
        	categoryServ.save(category);
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/servers/{server_id}/categories/edit")
    public String editsChannel(
            @ModelAttribute("category") Category category,
            BindingResult result, HttpSession session,
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
            // Get all categories for possible assignment ==========
            List<Category> categories = categoryServ.findAll();
            model.addAttribute("categories", categories);
            return "editCategories.jsp";
        }
    }


    @PostMapping("/servers/{server_id}/category/{category_id}/update")
    public String editCategory(
            @Valid @ModelAttribute("category") Category category,
            BindingResult result, HttpSession session,
            @PathVariable("server_id") Long server_id, 
            @PathVariable("category_id")Long category_id, 
            Model model) {
        if (result.hasErrors()) {
            Long user_id = (Long) session.getAttribute("uuid");
            User user = userService.findById(user_id);
            model.addAttribute("user", user);
        	// Server id for assignment ============
            Server server = serverServ.findById(server_id);
            model.addAttribute("server", server);
            // category Id for assignment =========
            Channel categoryLoad = channelServ.findById(category_id);
            model.addAttribute("categoryLoad", categoryLoad);
            // Get all categories for possible assignment ==========
            List<Category> categories = categoryServ.findAll();
            model.addAttribute("categories", categories);
            return "editChannel.jsp";
        } else {
        	categoryServ.updateCreate(category);
            return "redirect:/dashboard";
        }
    }
    
}