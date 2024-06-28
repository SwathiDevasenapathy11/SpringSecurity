package com.auth.service;

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

}
