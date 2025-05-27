package com.project.spring.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.spring.dto.ProfileDTO;
import com.project.spring.io.AuthRequest;
import com.project.spring.io.AuthResponse;
import com.project.spring.io.ProfileRequest;
import com.project.spring.io.ProfileResponse;
import com.project.spring.service.CustomUserDetailsService;
import com.project.spring.service.ProfileService;
import com.project.spring.service.util.JwtTokenUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Tag(name = "Auth Constroller", description = "Auth Controller")
@RequiredArgsConstructor
public class AuthController {
	private final ModelMapper modelMapper;
	private final ProfileService profileService;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenUtil jwtTokenUtil;
	private final CustomUserDetailsService userDetailsService;
	
	/**
	 * API endpoint to register new user	
	 * @param profileRequest
	 * @return profileResponse
	 */
	@Operation(summary = "Create new profile",
				description = "Returns a profile response")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/register")
	public ProfileResponse createProfile(@Valid @RequestBody ProfileRequest profileRequest) {
		log.info("API /register is called {}", profileRequest);
		ProfileDTO profileDTO = modelMapper.map(profileRequest, ProfileDTO.class);
		profileDTO = profileService.createProfile(profileDTO);
		log.info("Printing the profile DTO details {}", profileDTO);
		return modelMapper.map(profileDTO, ProfileResponse.class);
	}
	
	@PostMapping("/login")
	public AuthResponse authenticateProfile(@RequestBody AuthRequest authRequest) throws Exception {
		log.info("API /login is called {}", authRequest);
		authenticate(authRequest);
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
		final String token = jwtTokenUtil.generateToken(userDetails);
		jwtTokenUtil.generateToken(userDetails);
		return new AuthResponse(token, authRequest.getEmail());
	}
	
	private void authenticate(AuthRequest authRequest) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
		} catch (DisabledException ex) {
			throw new Exception("Profile disabled");
		} catch (BadCredentialsException ex) {
			throw new Exception("Bad Credentials");
		}
	}
}
