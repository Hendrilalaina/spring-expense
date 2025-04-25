package com.project.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.spring.entity.ProfileEntity;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

}
