package com.project.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.project.spring.service.CustomUserDetailsService;

@Configuration
public class WebSecurityConfig {
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	  
    @Bean
    JwtRequestFilter authenticationJwtTokenFilter() {
    	return new JwtRequestFilter();
    }
    
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    	return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    DaoAuthenticationProvider authenticationProvider() {
    	DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    	authProvider.setUserDetailsService(customUserDetailsService);
    	authProvider.setPasswordEncoder(passwordEncoder());
    	return authProvider;
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
}
