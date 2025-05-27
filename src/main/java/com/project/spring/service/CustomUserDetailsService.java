package com.project.spring.service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.spring.entity.ProfileEntity;
import com.project.spring.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final ProfileRepository profileRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		ProfileEntity profile = profileRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Profile not found for the email " + email));
		log.info("Inside loadUserByUsername():: Printing the profile details {}", profile);
		return new User(profile.getEmail(), profile.getPassword(), new ArrayList<>());
	}
	
}
