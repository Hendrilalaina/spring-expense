package com.project.spring.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	return http.csrf(csrf -> csrf.disable())
    			.authorizeHttpRequests(auth -> auth
                		.requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html")
                		.permitAll()
                		.anyRequest()
                		.authenticated())
                .formLogin(form -> form.disable())
                .httpBasic(withDefaults())
                .build();
    }
}
