package com.cohort.disqord.services;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.cohort.disqord.models.LoginUser;
import com.cohort.disqord.models.User;
import com.cohort.disqord.repositories.UserRepository;


@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepo;
    
    public User register(User newUser, BindingResult result) {
        if(userRepo.findByEmail(newUser.getEmail()).isPresent()) {
            result.rejectValue("email", "Unique", "This email is already in use!");
        }
        if(!newUser.getPassword().equals(newUser.getConfirm())) {
            result.rejectValue("confirm", "Matches", "The Confirm Password must match Password!");
        }
        if(result.hasErrors()) {
            return null;
        } else {
            String hashed = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
            newUser.setPassword(hashed);
            return userRepo.save(newUser);
        }
    }
    
    public User login(LoginUser newLogin, BindingResult result) {
        if(result.hasErrors()) {
            return null;
        }
        Optional<User> potentialUser = userRepo.findByEmail(newLogin.getEmail());
        if(!potentialUser.isPresent()) {
        } else {
        	User user = potentialUser.get();
        	if(!BCrypt.checkpw(newLogin.getPassword(), user.getPassword())) {
        		result.rejectValue("password", "Matches", "Invalid Password!");
        	}
        	if(result.hasErrors()) {
        		return null;
        	} else {
        		return user;
        	}        	
        }
        Optional<User> potentialUser1 = userRepo.findByUserName(newLogin.getEmail());
        if (!potentialUser1.isPresent()) {
        	result.rejectValue("email", "Unique", "Unknown username or email!");
        	return null;
        } else {
        	User user = potentialUser1.get();
        	if(!BCrypt.checkpw(newLogin.getPassword(), user.getPassword())) {
        		result.rejectValue("password", "Matches", "Invalid Password!");
        	}
        	if(result.hasErrors()) {
        		return null;
        	} else {
        		return user;
        	}    
        }
    }
    
    
    // We had to add this in order to get session id //
    public User getOne(Long id) {
    	Optional<User> user = userRepo.findById(id);
    	
    	if (user.isPresent()) {
    		return user.get();
    	} else {
    		return null;
    	}
    }
    
    public List<User> allUsers() {
        return userRepo.findAll();
    }
    
	public User updateUser(User user) {
		
		return userRepo.save(user);
	}
	
	public void deleteUser(Long id) {
		userRepo.deleteById(id);
	}

	public User findById(Long id) {
    	Optional<User> user = userRepo.findById(id);
    	
    	if (user.isPresent()) {
    		return user.get();
    	} else {
    		return null;
    	}
	}

	public User findByUserName(String userName) {
		Optional<User> user = userRepo.findByUserName(userName);
    	if (user.isPresent()) {
    		return user.get();
    	} else {
    		return null;
    	}
	}
	
	public User findByEmail(String email) {
		Optional<User> user = userRepo.findByEmail(email);
    	if (user.isPresent()) {
    		return user.get();
    	} else {
    		return null;
    	}
	}
	
	// Friends
	public void addFriend(User user, User friend) {
		user.getFriends().add(friend);
		userRepo.save(user);
	}
	
	public void removeFriend(User user, User friend) {
		user.getFriends().remove(friend);
		userRepo.save(user);
	}
	
	// Ajax / friend search stuff
	public List<User> ajaxSearch(String name){
		List<User> result = userRepo.findByUserNameLike(name);
		return result;
	}
    
    
}
