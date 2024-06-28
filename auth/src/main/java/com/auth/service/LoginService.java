package com.auth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth.entity.LoginInfo;
import com.auth.entity.User;
import com.auth.repository.IUserRepository;

@Service
public class LoginService implements UserDetailsService{
	
	@Autowired
	private IUserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userInfo = userRepository.findByEmail(username);
		return userInfo.map(LoginInfo::new).orElseThrow(() -> new UsernameNotFoundException(username));
	}
}
