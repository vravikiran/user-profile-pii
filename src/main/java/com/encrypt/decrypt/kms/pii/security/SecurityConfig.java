package com.encrypt.decrypt.kms.pii.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.encrypt.decrypt.kms.pii.util.JwtTokenFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	@Autowired
	JwtTokenFilter jwtTokenFilter;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws AccessDeniedException, Exception {
		return http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(requests -> requests
						.requestMatchers("/auth/**", "/userprofile/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
						.anyRequest().authenticated())
				.sessionManagement(
						sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class).build();
	}
}
