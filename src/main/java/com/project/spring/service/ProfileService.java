package com.project.spring.service;

import com.project.spring.dto.ProfileDTO;

public interface ProfileService {
	
	/**
	 * It will save the user details to database
	 * @param profileDTO
	 * @return profileDTO
	 */
	ProfileDTO createProfile(ProfileDTO profileDTO);
	
}
