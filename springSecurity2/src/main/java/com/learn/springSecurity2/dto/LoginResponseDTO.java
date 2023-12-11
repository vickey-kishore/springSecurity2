package com.learn.springSecurity2.dto;

import com.learn.springSecurity2.models.ApplicationUser;

public class LoginResponseDTO {

	private ApplicationUser user;
	private String jwt;

	public LoginResponseDTO() {
		super();
	}

	public LoginResponseDTO(ApplicationUser user, String jwt) {
		super();
		this.user = user;
		this.jwt = jwt;
	}

	public ApplicationUser getUser() {
		return user;
	}

	public void setUser(ApplicationUser user) {
		this.user = user;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	
}
