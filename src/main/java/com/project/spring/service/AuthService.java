package com.project.spring.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.spring.entity.ProfileEntity;
import com.project.spring.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final ProfileRepository profileRepository;
	
	/**
	 * Fetch the logged in profile
	 * @return ProfileEntity
	 */
	public ProfileEntity getLoggedInProfile() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final String email = authentication.getName();
		return profileRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("Profile not found for the email " + email ));
	}
}
