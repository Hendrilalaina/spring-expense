package com.project.spring.service.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {
	private static final long JWT_TOKEN_VALIDITY = 10 * 60 * 60;
	
//	@Value("${jwt.secret}")
	private String secret = "SPRING";
	
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return Jwts.builder()
			.setClaims(claims)
			.setSubject(userDetails.getUsername())
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
			.signWith(SignatureAlgorithm.HS512, secret)
			.compact();
	}

	public String generateUsernameFromToken(String jwtToken) {
		return getClaimFromToken(jwtToken, Claims::getSubject);
	}
	
	private <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
		final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
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
