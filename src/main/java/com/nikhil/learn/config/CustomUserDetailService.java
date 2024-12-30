package com.nikhil.learn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nikhil.learn.entity.User;
import com.nikhil.learn.exception.ResourceNotFoundException;
import com.nikhil.learn.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User userByEmail = userRepo.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User not found by given email "+username));
		
		return new CustomUserDetail(userByEmail);
	}

}
