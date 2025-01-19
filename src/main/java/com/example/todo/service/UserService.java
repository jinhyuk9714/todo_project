package com.example.todo.service;

import com.example.todo.dto.AuthRequest;
import com.example.todo.dto.UserRequest;
import com.example.todo.entity.User;
import com.example.todo.repository.UserRepository;
import com.example.todo.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // 이 클래스가 Spring의 서비스 계층 역할을 수행함을 표시
public class UserService {

    private final UserRepository userRepository; // 사용자 데이터베이스 액세스
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화 및 비교
    private final JwtUtil jwtUtil; // JWT 생성 및 처리

    // 생성자를 통해 의존성 주입
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 회원가입 처리
     *
     * @param userRequest 사용자 요청 데이터 (사용자명, 비밀번호 포함)
     */
    public void register(UserRequest userRequest) {
        // 사용자명 중복 검사
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists"); // 중복 시 예외 발생
        }

        // 새 사용자 엔티티 생성
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword())); // 비밀번호 암호화 저장
        user.setRole("USER"); // 기본 역할 설정

        userRepository.save(user); // 데이터베이스에 사용자 저장
    }

    /**
     * 사용자 인증 처리 (로그인)
     *
     * @param authRequest 인증 요청 데이터 (사용자명, 비밀번호 포함)
     * @return JWT 토큰 (성공 시) 또는 null (실패 시)
     */
    public String authenticate(AuthRequest authRequest) {
        // 사용자명으로 사용자 조회
        Optional<User> userOptional = userRepository.findByUsername(authRequest.getUsername());

        // 사용자 존재 여부 확인
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // 입력된 비밀번호가 저장된 해시 비밀번호와 일치하는지 확인
            if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                // 인증 성공 시 JWT 토큰 생성 및 반환
                return jwtUtil.generateToken(user.getUsername());
            }
        }

        // 인증 실패 시 null 반환
        return null;
    }
}
