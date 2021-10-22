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
import com.cohort.disqord.models.LoginUser;
import com.cohort.disqord.models.User;
import com.cohort.disqord.services.ChatRoomNotificationServ;
import com.cohort.disqord.services.UserService;

@Controller
public class HomeController {
    
    @Autowired
    private UserService userServ;
    
    @Autowired
    ChatRoomNotificationServ chatRoomNotificationServ;
    
    @GetMapping("/")
    public String index(Model model, HttpSession session) {
    	if (session.getAttribute("uuid") != null) {
    		return "redirect:/dashboard";
    	}
        model.addAttribute("newUser", new User());
        model.addAttribute("newLogin", new LoginUser());
        return "/Users/index.jsp";
    }
    
    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
    	if (session.getAttribute("uuid") != null) {
    		return "redirect:/dashboard";
    	}
    	
        model.addAttribute("newUser", new User());
        model.addAttribute("newLogin", new LoginUser());
        return "/Users/index.jsp";
    }
    
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("newUser") User newUser, 
            BindingResult result, Model model, HttpSession session) {
        userServ.register(newUser, result);
        if(result.hasErrors()) {
            model.addAttribute("newLogin", new LoginUser());
            return "/Users/index.jsp";
        }
        session.setAttribute("uuid", newUser.getId());
        return "redirect:/dashboard";
    }
    
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin, 
            BindingResult result, Model model, HttpSession session) {
        User user = userServ.login(newLogin, result);
        if(result.hasErrors()) {
            model.addAttribute("newUser", new User());
            return "/Users/index.jsp";
        }
        session.setAttribute("uuid", user.getId());
        return "redirect:/dashboard";
    }
    
	@GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
    	if (session.getAttribute("uuid") != null) {
    		Long id = (Long) session.getAttribute("uuid");
    		User user = userServ.findById(id);
    		model.addAttribute("user", user);
    		return "/Users/dashboard.jsp";
    	} else {
    		return "redirect:/";    		
    	}
	}
    
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
    	session.removeAttribute("uuid");
    	
    	return "redirect:/";
    }
    
//  Add friend
    @GetMapping("/friends/add/{friendId}")
    public String addFriend(Model model, HttpSession session, @PathVariable("friendId") Long friendId) {
    	User user = userServ.findById((long)session.getAttribute("uuid"));
    	User friend = userServ.findById(friendId);
    	if(user.getFriends().contains(friend)) {
    		return "redirect:/dashboard";
    	} else {
    		userServ.addFriend(user, friend);
    		return "redirect:/dashboard";    		
    	}
    }
    
//	Remove friend
    @DeleteMapping("/friends/remove/{friendId}")
    public String removeFriend(HttpSession session, @PathVariable("friendId") Long friendId) {
    	User user = userServ.findById((long)session.getAttribute("uuid"));
    	User friend = userServ.findById(friendId);
    	if(user.getFriends().contains(friend)) {
    		userServ.removeFriend(user, friend);
    		return "redirect:/dashboard";
    	} else {
    		return "redirect:/dashboard";    		
    	}
    }
    
}
