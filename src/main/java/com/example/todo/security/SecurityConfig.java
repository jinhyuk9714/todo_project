package com.example.todo.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    // JwtAuthenticationFilter 주입. JWT 인증을 처리하는 커스텀 필터.
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter; // 생성자를 통해 DI로 필터 주입.
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 보안 필터 체인 정의
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화. REST API의 특성상 상태를 유지하지 않기 때문에 불필요.

                // 요청 인증 규칙 정의
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/", "/frontend/**", "/auth/**", "/favicon.ico").permitAll()
                                // 위 경로들은 인증 없이 접근 가능 (예: 메인 페이지, 정적 리소스, 인증 API 등).
                                .anyRequest().authenticated()
                        // 나머지 모든 요청은 인증 필요.
                )

                // 세션 관리 정책 설정
                .sessionManagement(session ->
                                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        // 세션을 생성하지 않음. JWT 기반 인증에 적합.
                )

                // 인증 실패 시 처리 방식 설정
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            // 인증 실패 시 HTTP 401 상태 코드 반환.
                        })
                )

                // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
                .addFilterBefore(jwtAuthenticationFilter,
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build(); // 구성된 SecurityFilterChain 반환.
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 비밀번호 암호화를 위한 BCryptPasswordEncoder 빈 생성.
        return new BCryptPasswordEncoder();
        // BCrypt는 강력한 해시 함수로, 비밀번호 보안에 널리 사용됨.
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        // AuthenticationManager 빈 생성. Spring Security의 인증 관리 객체.
        return configuration.getAuthenticationManager();
        // 인증 작업(예: 로그인 처리)을 수행하는 데 사용.
    }
}
