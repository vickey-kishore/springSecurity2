package com.learn.springSecurity2.service;

import java.time.Instant;
import java.util.stream.Collectors;

import org.apache.logging.log4j.CloseableThreadContext.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

	@Autowired
	private JwtEncoder jwtEncoder;
	
	@Autowired
	private JwtDecoder jwtDecoder;
	
	public String generateJwt(Authentication auth) {
		
		Instant now = Instant.now(); //getting the creation time
		
		String scope = auth.getAuthorities().stream()  //hint: functional programming has been used
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(" "));
		
		JwtClaimsSet claims = JwtClaimsSet.builder() //JWT encoder uses claims to generate JWT token
				.issuer("self")
				.issuedAt(now)
				.subject(auth.getName())
				.claim("roles", scope)
				.build();
		
		return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}
}
