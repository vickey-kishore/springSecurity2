package com.learn.springSecurity2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.learn.springSecurity2.repository.UserRepo;

@Service
public class UserService implements UserDetailsService{

	// step-2: spring security encoder
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private UserRepo userRepo;
	
	
	//step-1:
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		System.out.println("inside user details service");
		
		return userRepo.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User is not found"));
	}

}
