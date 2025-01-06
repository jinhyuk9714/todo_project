package com.example.todo.security;

import io.jsonwebtoken.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Getter // Lombok의 Getter 어노테이션을 클래스에 추가
public class JwtUtil {

    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    @Value("${JWT_EXPIRATION_MS}")
    private long expirationMs;

    public String generateToken(String username) {
        // 현재 시간과 만료 시간 출력 (디버깅용)
        System.out.println("Issued At: " + new Date());
        System.out.println("Expiration: " + new Date(System.currentTimeMillis() + expirationMs));

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date()) // 현재 시간
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs)) // 만료 시간 설정
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("JWT expired", e);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
