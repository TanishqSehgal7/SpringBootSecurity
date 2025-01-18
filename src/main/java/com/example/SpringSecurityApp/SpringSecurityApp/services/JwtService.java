package com.example.SpringSecurityApp.SpringSecurityApp.services;

import com.example.SpringSecurityApp.SpringSecurityApp.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;

@Service
public class JwtService {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    private final Logger logger = LoggerFactory.getLogger(JwtService.class);

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("roles", Set.of("ADMIN", "USER"))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3*60*1000))
                .signWith(getSecretKey())
                .compact();
    }

    public Claims getClaims(String token) {

        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            logger.error("Error parsing token: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid token");
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        logger.info("Claims: " + claims.toString());
        return Long.valueOf(claims.getSubject());
    }

    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, String userId) {
        return (userId.equals(getUserIdFromToken(token)) && !isTokenExpired(token));
    }
}
