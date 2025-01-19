package com.example.todo.security;

import io.jsonwebtoken.*; // JWT 생성 및 파싱을 위한 라이브러리
import lombok.Getter; // Lombok의 Getter 어노테이션 사용
import org.springframework.beans.factory.annotation.Value; // application.properties 값 주입
import org.springframework.stereotype.Component; // Spring 컴포넌트로 등록

import java.util.Date; // JWT 발급 및 만료 날짜 설정에 사용

@Component // 이 클래스를 Spring Bean으로 등록
@Getter // secretKey와 expirationMs에 대한 getter 자동 생성
public class JwtUtil {

    @Value("${JWT_SECRET_KEY}") // application.properties에서 JWT 비밀 키 로드
    private String secretKey;

    @Value("${JWT_EXPIRATION_MS}") // application.properties에서 JWT 만료 시간 로드
    private long expirationMs;

    /**
     * JWT 토큰 생성
     * @param username JWT 토큰에 포함할 사용자 이름
     * @return 생성된 JWT 토큰 문자열
     */
    public String generateToken(String username) {
        // 현재 시간과 만료 시간 출력 (디버깅용)
        System.out.println("Issued At: " + new Date());
        System.out.println("Expiration: " + new Date(System.currentTimeMillis() + expirationMs));

        // JWT 토큰 생성
        return Jwts.builder()
                .setSubject(username) // 토큰에 사용자 이름 설정
                .setIssuedAt(new Date()) // 토큰 발급 시간 설정
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs)) // 토큰 만료 시간 설정
                .signWith(SignatureAlgorithm.HS512, secretKey) // HS512 알고리즘으로 비밀 키를 사용해 서명
                .compact(); // 최종적으로 토큰 생성
    }

    /**
     * JWT 토큰에서 사용자 이름 추출
     * @param token 클라이언트로부터 받은 JWT 토큰
     * @return 토큰에서 추출한 사용자 이름
     */
    public String extractUsername(String token) {
        try {
            // 토큰을 파싱하고 클레임에서 사용자 이름(subject) 추출
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey) // 서명을 검증하기 위해 비밀 키 설정
                    .build()
                    .parseClaimsJws(token) // JWT 파싱
                    .getBody() // 클레임(body) 가져오기
                    .getSubject(); // subject(사용자 이름) 추출
        } catch (ExpiredJwtException e) {
            // 토큰이 만료된 경우 처리
            throw new RuntimeException("JWT expired", e);
        } catch (Exception e) {
            // 잘못된 토큰 처리
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    /**
     * JWT 토큰 유효성 검증
     * @param token 클라이언트로부터 받은 JWT 토큰
     * @return 토큰이 유효하면 true, 아니면 false
     */
    public boolean validateToken(String token) {
        try {
            // 토큰 파싱 및 서명 검증
            Jwts.parserBuilder()
                    .setSigningKey(secretKey) // 비밀 키로 서명을 검증
                    .build()
                    .parseClaimsJws(token); // JWT 파싱
            return true; // 검증 성공
        } catch (JwtException | IllegalArgumentException e) {
            // 잘못된 토큰 또는 기타 오류 처리
            return false;
        }
    }
}
