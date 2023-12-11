package com.learn.springSecurity2;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.learn.springSecurity2.models.ApplicationUser;
import com.learn.springSecurity2.models.Role;
import com.learn.springSecurity2.repository.RoleRepo;
import com.learn.springSecurity2.repository.UserRepo;

@SpringBootApplication
public class SpringSecurity2Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurity2Application.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepo roleRepo, UserRepo userRepo, PasswordEncoder passwordEncoder) {
		return args -> {
			
			if(roleRepo.findByAuthority("admin").isPresent()) {return;} //checking role is present or not
			
			Role adminRole = roleRepo.save(new Role ("ADMIN"));
			
			roleRepo.save(new Role("USER"));
			
			Set<Role> roles = new HashSet<>();
			roles.add(adminRole);
			
			ApplicationUser admin = new ApplicationUser(1, "admin", passwordEncoder.encode("password"), roles);
			
			userRepo.save(admin);
		};
	}
}
