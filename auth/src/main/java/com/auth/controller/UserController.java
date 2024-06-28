package com.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.dto.AuthRequest;
import com.auth.entity.User;
import com.auth.service.JwtService;
import com.auth.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	public AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtService jwtService;
	
	@PostMapping("/add")
	public ResponseEntity<?> add(@RequestBody User user) {
		
		if(user.getRole()==null) {
			user.setRole("USER");
		}
		User userCheck = userService.add(user);
		if(userCheck == null) {
			return new ResponseEntity<>("Email Already Exists" , HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(userCheck , HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public String authentication(@RequestBody AuthRequest authRequest) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		if(authentication.isAuthenticated()) {
			return jwtService.generateToken(authRequest.getUsername());
		}else {
			throw new UsernameNotFoundException("User not found");
		}
	}
	
//	@PostMapping("/login")
//	public String authentication(@RequestBody AuthRequest authRequest) {
//	    try {
//	        Authentication authentication = authenticationManager.authenticate(
//	            new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
//	        if(authentication.isAuthenticated()) {
//	            return jwtService.generateToken(authRequest.getUsername());
//	        } else {
//	            throw new UsernameNotFoundException("User not found");
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        throw new UsernameNotFoundException("User not found");
//	    }
//	}

}
