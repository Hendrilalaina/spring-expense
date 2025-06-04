package com.project.spring.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.spring.dto.ProfileDTO;
import com.project.spring.io.AuthRequest;
import com.project.spring.io.AuthResponse;
import com.project.spring.io.ProfileRequest;
import com.project.spring.io.ProfileResponse;
import com.project.spring.io.RefreshTokenRequest;
import com.project.spring.service.CustomUserDetailsService;
import com.project.spring.service.ProfileService;
import com.project.spring.service.TokenBlackistService;
import com.project.spring.service.util.JwtTokenUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Tag(name = "Auth Constroller", description = "Auth Controller")
@RequiredArgsConstructor
public class AuthController {
	private final ModelMapper modelMapper;
	private final ProfileService profileService;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenUtil jwtTokenUtil;
	private final CustomUserDetailsService userDetailsService;
	private final TokenBlackistService tokenBlacklistService;
	
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
	
	/**
	 * API endpoint to login user
	 * @param authRequest
	 * @return authResponse
	 * @throws Exception
	 */
	@Operation(summary = "Login an user",
			description = "Returns an authentication response")
	@PostMapping("/login")
	public AuthResponse authenticateProfile(@RequestBody AuthRequest authRequest) throws Exception {
		log.info("API /login is called {}", authRequest);
		authenticate(authRequest);
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
		final String token = jwtTokenUtil.generateToken(userDetails);
		final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
		return new AuthResponse(token, refreshToken, authRequest.getEmail());
	}
	
	/**
	 * API to logout an authenticated user
	 * @param request
	 */
	@Operation(summary = "Logout an authenticated user")
	@PostMapping("/signout")
	public ResponseEntity<?> signout(HttpServletRequest request) {
		String jwtToken = extractJwtTokenFromRequest(request);
		log.info("API /signout is called with token {}", jwtToken);
		if (jwtToken != null) {
			tokenBlacklistService.addTokenToBlacklist(jwtToken);
		}
		return ResponseEntity.ok("Log out successfully");
	}
	
	/**
	 * API to refresh token
	 * @param refreshToken
	 * @return
	 * @throws Exception 
	 */
	@Operation(summary = "Refresh token")
	@PostMapping("/refresh-token")
	public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        log.info("API /refresh-token is called with {}", refreshToken);
        // Validate refresh token
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body(new AuthResponse(null, null, null));
        }

        try {
            // Extract username from refresh token
            String username = jwtTokenUtil.getUsernameFromToken(refreshToken);

            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate refresh token
            if (jwtTokenUtil.validateToken(refreshToken, userDetails)) {
                // Generate new access token
                String newAccessToken = jwtTokenUtil.generateToken(userDetails);

                // Optionally generate a new refresh token (rotating refresh tokens)
                String newRefreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

                // Return new tokens
                AuthResponse authResponse = new AuthResponse(newAccessToken, newRefreshToken, username);
                return ResponseEntity.ok(authResponse);
            } else {
                return ResponseEntity.status(401).body(new AuthResponse(null, null, null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new AuthResponse(null, null, null));
        }
    }

	
	private String extractJwtTokenFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
	
	private void authenticate(AuthRequest authRequest) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
		} catch (DisabledException ex) {
			throw new Exception("Profile disabled");
		} catch (BadCredentialsException ex) {
			throw new Exception("Bad Credentials");
		}
	}
}
