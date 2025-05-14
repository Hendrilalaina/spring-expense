package com.project.spring.io;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {
	private String profileId;
	
	private String email;
	
	private String name;
	
	private Timestamp createdAt;
	
	private Timestamp updatedAt;
}
