package com.project.spring.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.spring.dto.ProfileDTO;
import com.project.spring.io.ProfileRequest;
import com.project.spring.io.ProfileResponse;
import com.project.spring.service.ProfileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Tag(name = "Auth Constroller", description = "Auth Controller")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {
	private final ModelMapper modelMapper;
	private final ProfileService profileService;
	
	
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
}
