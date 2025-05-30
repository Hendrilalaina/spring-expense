package com.project.spring.io;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
	private String token;
	private String refreshToken;
	private String email;
}
