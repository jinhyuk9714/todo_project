package com.example.todo.security;

import com.example.todo.entity.User; // 사용자 엔티티 클래스
import com.example.todo.repository.UserRepository; // 사용자 정보를 조회하기 위한 리포지토리
import org.springframework.security.core.userdetails.UserDetails; // Spring Security에서 사용하는 사용자 정보 인터페이스
import org.springframework.security.core.userdetails.UserDetailsService; // 사용자 정보를 제공하기 위한 인터페이스
import org.springframework.security.core.userdetails.UsernameNotFoundException; // 사용자 이름을 찾지 못했을 때 발생하는 예외
import org.springframework.stereotype.Service; // Spring의 서비스 계층 어노테이션

import java.util.Collections; // 권한이 없을 때 빈 리스트를 반환하기 위해 사용

/**
 * Spring Security에서 인증을 위해 사용자 정보를 로드하는 서비스 클래스.
 */
@Service // 이 클래스를 Spring의 서비스 컴포넌트로 등록
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository; // 사용자 정보를 데이터베이스에서 조회하기 위한 리포지토리

    /**
     * 생성자 주입을 통해 UserRepository를 전달받음.
     * @param userRepository 사용자 정보를 관리하는 리포지토리
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 사용자 이름을 기반으로 사용자 정보를 로드.
     * Spring Security의 인증 과정에서 호출됨.
     * @param username 조회할 사용자 이름
     * @return Spring Security에서 사용하는 UserDetails 객체
     * @throws UsernameNotFoundException 사용자 이름을 찾을 수 없는 경우 발생
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 이름으로 User 엔티티를 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // 조회된 사용자 정보를 기반으로 Spring Security의 UserDetails 객체를 생성 및 반환
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), // 사용자 이름
                user.getPassword(), // 비밀번호 (암호화된 상태)
                Collections.emptyList() // 사용자 권한 정보 (현재는 빈 리스트로 설정)
        );
    }
}
