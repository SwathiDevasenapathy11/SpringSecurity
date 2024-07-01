package com.auth.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.entity.User;
import com.auth.repository.IUserRepository;

@Service
public class UserService {
	
	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public User add(User user) {
		if(userRepository.existsByEmail(user.getEmail())) {
			return null;
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
		return userRepository.save(user);
	}
	
	public List<User> get(){
		return userRepository.findAll();
	}
	
	public User update(User user) {
		Optional<User> updateUser = userRepository.findById(user.getId());
		if(updateUser.isPresent()) {
			User existingUser = updateUser.get();
			existingUser.setEmail(user.getEmail());
			existingUser.setUsername(user.getUsername());
			return userRepository.save(existingUser);
		} else {
			throw new RuntimeException("No data found with that id");
		}
//		return null;
	}
	
	public void delete(Long id) {
		 userRepository.deleteById(id);
	}

}
