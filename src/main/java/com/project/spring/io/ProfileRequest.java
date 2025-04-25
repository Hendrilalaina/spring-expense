package com.project.spring.io;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileRequest {
	@NotBlank(message = "Name is required")
	@Size(min = 3, message = "Name should be at least 3 characters")
	private String name;
	
	@NotNull(message = "Email is required")
	@Email(message = "Provid valid email address")
	private String email;
	
	@NotNull(message = "Password is required")
	@Size(min = 5, message = "Password should be at least 5 characters")
	private String password;
}
