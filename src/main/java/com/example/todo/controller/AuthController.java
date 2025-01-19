package com.example.todo.controller;

import com.example.todo.dto.AuthRequest;
import com.example.todo.dto.AuthResponse;
import com.example.todo.dto.RegisterRequest;
import com.example.todo.entity.User;
import com.example.todo.repository.UserRepository;
import com.example.todo.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// 컨트롤러 클래스 선언
@RestController
@RequestMapping("/auth")
public class AuthController {

    // 의존성 선언
    private final AuthenticationManager authenticationManager; // 사용자 인증 처리
    private final UserRepository userRepository; // 사용자 데이터베이스 접근
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화
    private final JwtUtil jwtUtil; // JWT 생성 및 검증

    // 생성자를 통한 의존성 주입
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // 회원가입 처리
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        // 사용자 이름 중복 확인
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // HTTP 400 상태 반환
                    .body(Map.of("message", "이미 존재하는 사용자 이름입니다.")); // 오류 메시지 반환
        }

        // 새로운 사용자 생성 및 데이터 설정
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 비밀번호 암호화

        // 사용자 저장
        userRepository.save(user);

        // 성공 메시지 반환
        return ResponseEntity.ok(Map.of("message", "회원가입이 성공적으로 완료되었습니다."));
    }

    // 로그인 처리
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            // 사용자 인증
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            // 인증 성공 시 JWT 생성
            String token = jwtUtil.generateToken(authRequest.getUsername());

            // JWT 만료 시간 계산
            long expirationTime = System.currentTimeMillis() + jwtUtil.getExpirationMs();

            // JWT 및 만료 시간 반환
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "expiresAt", expirationTime
            ));
        } catch (Exception e) {
            // 인증 실패 시 401 상태 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    // JWT 토큰 유효성 검증
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        // Authorization 헤더에서 "Bearer " 접두사 확인 후 토큰 추출
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 이후의 값
            if (jwtUtil.validateToken(token)) { // JWT 유효성 검증
                return ResponseEntity.ok(Map.of("message", "Token is valid")); // 유효 시 성공 메시지 반환
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token"); // 유효하지 않으면 401 상태 반환
    }
}