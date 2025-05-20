package com.example.demo.security;


import io.jsonwebtoken.*;

import org.springframework.stereotype.Component;



import java.util.Date;


@Component
public class JwtUtil {
    private final String secret = "your_jwt_secret_key";
    private final long expirationMs = 3600000; // 1 hour

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
        	return false;
        }
    }
}
        