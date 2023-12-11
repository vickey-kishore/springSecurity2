package com.learn.springSecurity2.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.learn.springSecurity2.util.RSAKeyProperties;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class SecurityConfig {
	
	//step-4:
	private final RSAKeyProperties keys;
	
	public SecurityConfig(RSAKeyProperties keys) {
		this.keys = keys;
	}
	
	
	// step-1:  creating instance of the filter chain
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
		//return httpSecurity //step-6.1:
		httpSecurity //step-6.1:
				.csrf(csrf -> csrf.disable())
				//.authorizeHttpRequests(auth -> auth.anyRequest().authenticated()) // **** note-1:
				.authorizeHttpRequests(auth -> {
					//auth.requestMatchers("/auth/**").permitAll(); // !!! it is not working
					auth.requestMatchers(AntPathRequestMatcher.antMatcher("/auth/**")).permitAll(); //allowing "/auth" and its nested routes "**" and permitting all users
					auth.requestMatchers(AntPathRequestMatcher.antMatcher("/admin/**")).hasRole("ADMIN"); //step-6.1:
					auth.requestMatchers(AntPathRequestMatcher.antMatcher("/admin/**")).hasAnyRole("ADMIN","USER"); //step-6.1:
					auth.anyRequest().authenticated();
				}); //step-6.1: adding semi-colon
				//.httpBasic().and() // !!! removing after step 4.2 ---> note:2
				
		httpSecurity //step-6.1:
				//step-5: from now using OAuth
				//.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)//step-6.1:
				.oauth2ResourceServer()//step-6.1: from here
					.jwt()
					.jwtAuthenticationConverter(jwtAuthConverter());
		httpSecurity //step-6.1:
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
				//.build();//step-6.1:
		
		return httpSecurity.build();//step-6.1:
	}
	
	// step-2: creating password encoder
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// step-3: creating authentication manager
	@Bean
	public AuthenticationManager authManager (UserDetailsService detailsService) {
		DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
		daoProvider.setUserDetailsService(detailsService);
		daoProvider.setPasswordEncoder(passwordEncoder());
		return new ProviderManager(daoProvider);
	}
	
	//step-4.1: creating decoder
	@Bean
	public JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build();
	}
	
	//step-4.2: creating encoder
	@Bean
	public JwtEncoder jwtEncoder() {
		JWK jwk = new RSAKey
					.Builder(keys.getPublicKey())
					.privateKey(keys.getPrivateKey())
					.build();
		
		JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
		
		return new NimbusJwtEncoder(jwks);
	}
	
	//step-6: converting JWT to get role and appending role_
		@Bean
		public JwtAuthenticationConverter jwtAuthConverter() {
			JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
			jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
			jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
			
			JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
			jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
			
			return jwtConverter;
		}
}


	

/*
notes:
	1) .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
		- The above line will throw 401 error when we try to register as a new user, 
		  because it will only allow the authenticated user to enter.
	
	2) .httpBasic().and() -> we wont be going to use this. because from now we are going to use OAuth.

*/
