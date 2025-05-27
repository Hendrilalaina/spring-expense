package com.project.spring.service.impl;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.spring.dto.ProfileDTO;
import com.project.spring.entity.ProfileEntity;
import com.project.spring.exception.ItemExistsException;
import com.project.spring.repository.ProfileRepository;
import com.project.spring.service.ProfileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{
	private final ProfileRepository profileRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder encoder;
	
	/**
	 * It will save the user details to database
	 * @param profileDTO
	 * @return profileDTO
	 */
	@Override
	public ProfileDTO createProfile(ProfileDTO profileDTO) {
		if (profileRepository.existsByEmail(profileDTO.getEmail())) {
			throw new ItemExistsException("Email already exists" + profileDTO.getEmail());
		}
		profileDTO.setPassword(encoder.encode(profileDTO.getPassword()));
		ProfileEntity profileEntity = modelMapper.map(profileDTO, ProfileEntity.class);
		profileEntity.setProfileId(UUID.randomUUID().toString());
		profileEntity = profileRepository.save(profileEntity);
		log.info("Printing the profile entity details {}", profileEntity);
		return modelMapper.map(profileEntity, ProfileDTO.class);
	}

}
