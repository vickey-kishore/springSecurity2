package com.learn.springSecurity2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.springSecurity2.dto.LoginResponseDTO;
import com.learn.springSecurity2.dto.RegistrationDTO;
import com.learn.springSecurity2.models.ApplicationUser;
import com.learn.springSecurity2.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {

	@Autowired
	private AuthenticationService authService;

	@PostMapping("/register")
	public ApplicationUser registerUser (@RequestBody RegistrationDTO registration) {
		return authService.registerUser(registration.getUsername(), registration.getPassword());
	}
	
	@PostMapping("/login")
	public LoginResponseDTO loginUser (@RequestBody RegistrationDTO registration) {
		return authService.loginUser(registration.getUsername(), registration.getPassword());
	}
}
