package com.project.spring.service.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil {
	private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		return Jwts.builder()
			.setClaims(claims)
			.setSubject(userDetails.getUsername())
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
			.signWith(key)
			.compact();
	}

	public String generateUsernameFromToken(String jwtToken) {
		return getClaimFromToken(jwtToken, Claims::getSubject);
	}
	
	private <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
		SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		final Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		return claimResolver.apply(claims);
	}
	
	public boolean validateToken(String jwtToken, UserDetails userDetails) {
		final String email = this.generateUsernameFromToken(jwtToken);
		return email.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken);
	}
	
	private boolean isTokenExpired(String jwtToken) {
		final Date expiration = this.getClaimFromToken(jwtToken, Claims::getExpiration);
		return expiration.before(new Date());
	}
}
