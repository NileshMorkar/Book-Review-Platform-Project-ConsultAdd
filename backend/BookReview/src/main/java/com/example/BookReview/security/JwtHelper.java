package com.example.BookReview.security;

import com.example.BookReview.dto.response.JwtResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class JwtHelper {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.token-validity}")
    private long jwtTokenValidityInSeconds;

    /**
     * Generate signing key using secret
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Generate JWT token with optional claims
     */
    public JwtResponse generateToken(UserDetails userDetails) {
        log.info("ROLE = {}", userDetails.getAuthorities());

        Map<String, String> claims = new HashMap<>();
        claims.put("ROLE", userDetails.getAuthorities().toArray()[0].toString());

        return generateToken(claims, userDetails.getUsername());
    }

    /**
     * Generate JWT token with optional claims
     */
    public JwtResponse generateToken(Map<String, String> claims, String subject) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtTokenValidityInSeconds * 1000);

        return JwtResponse.builder()
                .jwtToken(Jwts.builder()
                        .setClaims(claims)
                        .setSubject(subject)
                        .setIssuedAt(now)
                        .setExpiration(expiration)
                        .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                        .compact())
                .role(claims.get("ROLE"))
                .build();
    }

    /**
     * Validate token with user details
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Get username from token
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Get expiration date from token
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Get any claim using a resolver function
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Check if token is expired
     */
    private boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    /**
     * Parse and validate token signature to extract claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
