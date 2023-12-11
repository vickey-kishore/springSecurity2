package com.learn.springSecurity2.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learn.springSecurity2.dto.LoginResponseDTO;
import com.learn.springSecurity2.models.ApplicationUser;
import com.learn.springSecurity2.models.Role;
import com.learn.springSecurity2.repository.RoleRepo;
import com.learn.springSecurity2.repository.UserRepo;

@Service
@Transactional
public class AuthenticationService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenService tokenService;

	public ApplicationUser registerUser(String username, String password) {

		String encodePassword = encoder.encode(password);
		Role userRole = roleRepo.findByAuthority("USER").get();

		Set<Role> authorities = new HashSet<>();

		authorities.add(userRole);

		return userRepo.save(new ApplicationUser(0, username, encodePassword, authorities));
	}

	public LoginResponseDTO loginUser(String username, String password) {
		try {
			Authentication auth = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			
			String token = tokenService.generateJwt(auth);
			
			return new LoginResponseDTO(userRepo.findByUsername(username).get(), token); // using .get() because it will  return optional type, so to get the username get() is needed

		} catch (AuthenticationException e) {
			return new LoginResponseDTO(null, ""); // return as new LoginResponseDTO(user:null, jwt:"")
		}
	}

}
