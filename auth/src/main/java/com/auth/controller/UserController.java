package com.auth.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.dto.AuthRequest;
import com.auth.dto.AuthResponse;
import com.auth.entity.User;
import com.auth.repository.IUserRepository;
import com.auth.service.JwtService;
import com.auth.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private IUserRepository userRepository;
	
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
	
	@GetMapping("/get")
	@PreAuthorize("hasAuthority('ADMIN')")
	public List<User> get(){
		return userService.get();
	}
	
	@PutMapping("/update")
	@PreAuthorize("hasAuthority('USER')")
	public User update(@RequestBody User user) {
		return userService.update(user);
	}
	
	@DeleteMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {
		userService.delete(id);
		return "Deleted";
	}
	
	@PostMapping("/login")
	public AuthResponse authentication(@RequestBody AuthRequest authRequest) {
		AuthResponse response = new AuthResponse();
		Optional<User> user = userRepository.findByEmail(authRequest.getUsername());
		
		if(userRepository.existsByEmail(authRequest.getUsername())) {
			Authentication authenticatrion = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
			if(authenticatrion.isAuthenticated()) {
				response.setId(user.get().getId());
				response.setUsername(user.get().getUsername());
				response.setRole(user.get().getRole());
				response.setToken(jwtService.generateToken(authRequest.getUsername()));
				response.setResponse("Login Successfully");
				return response;
			}else {
				response.setResponse("Invalid login");
				return response;
			}
		}
//		response.setResponse("Login is not done");
		return response;
	}
}
