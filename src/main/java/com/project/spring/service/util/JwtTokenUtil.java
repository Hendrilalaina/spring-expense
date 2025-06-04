package com.project.spring.service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
    private static final long JWT_TOKEN_VALIDITY = 60 * 1000;
    private static final long REFRESH_TOKEN_VALIDITY = 1 * 24 * 60 * 60 * 1000;

//    @Value("${jwt.secret}")
    private String secret = "SPRING";

    // Generate access token
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), JWT_TOKEN_VALIDITY);
    }

    // Generate refresh token
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), REFRESH_TOKEN_VALIDITY);
    }

    // Helper method to create tokens
    private String createToken(Map<String, Object> claims, String subject, long validity) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    // Refresh access token using refresh token
    public String refreshAccessToken(String refreshToken, UserDetails userDetails) {
        if (validateRefreshToken(refreshToken, userDetails)) {
            return generateToken(userDetails); // Generate new access token
        }
        throw new IllegalArgumentException("Invalid or expired refresh token");
    }

    // Validate refresh token
    public boolean validateRefreshToken(String refreshToken, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(refreshToken);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(refreshToken);
        } catch (Exception e) {
            return false;
        }
    }

    // Extract username from token
    public String getUsernameFromToken(String jwtToken) {
        return getClaimFromToken(jwtToken, Claims::getSubject);
    }

    // Generic method to extract claims from token
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
        final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claimResolver.apply(claims);
    }

    // Validate access token
    public boolean validateToken(String jwtToken, UserDetails userDetails) {
        final String email = getUsernameFromToken(jwtToken);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken);
    }

    // Check if token is expired
    private boolean isTokenExpired(String jwtToken) {
        try {
            final Date expiration = getClaimFromToken(jwtToken, Claims::getExpiration);
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}