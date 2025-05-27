package com.project.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.spring.entity.ProfileEntity;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
	Optional<ProfileEntity> findByEmail(String email);
	Boolean existsByEmail(String email);
}
