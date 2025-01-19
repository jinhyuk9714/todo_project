package com.example.todo.security;

import io.jsonwebtoken.ExpiredJwtException; // JWT 토큰 만료 시 발생하는 예외
import io.jsonwebtoken.JwtException; // JWT 관련 일반적인 예외
import jakarta.servlet.FilterChain; // 필터 체인 객체
import jakarta.servlet.ServletException; // 서블릿 관련 예외
import jakarta.servlet.http.HttpServletRequest; // HTTP 요청 객체
import jakarta.servlet.http.HttpServletResponse; // HTTP 응답 객체
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Spring Security 인증 객체
import org.springframework.security.core.context.SecurityContextHolder; // 현재 인증 컨텍스트를 제공하는 클래스
import org.springframework.security.core.userdetails.UserDetails; // 사용자 정보 인터페이스
import org.springframework.stereotype.Component; // Spring 빈 등록
import org.springframework.web.filter.OncePerRequestFilter; // HTTP 요청마다 한 번 실행되는 필터

import java.io.IOException; // 입출력 예외

/**
 * JWT 인증 필터 클래스.
 * HTTP 요청마다 실행되며 JWT를 확인하고 인증을 처리.
 */
@Component // Spring이 관리하는 컴포넌트로 등록
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil; // JWT 토큰 유효성 검사 및 파싱을 위한 유틸리티
    private final CustomUserDetailsService customUserDetailsService; // 사용자 정보를 로드하기 위한 서비스

    /**
     * 생성자를 통해 필요한 의존성 주입.
     * @param customUserDetailsService 사용자 정보를 로드하는 서비스
     * @param jwtUtil JWT 관련 유틸리티
     */
    public JwtAuthenticationFilter(CustomUserDetailsService customUserDetailsService, JwtUtil jwtUtil) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 필터의 실제 로직 구현.
     * HTTP 요청에서 JWT를 추출하고, 검증 후 사용자 정보를 인증 컨텍스트에 설정.
     * @param request 클라이언트의 HTTP 요청
     * @param response 서버의 HTTP 응답
     * @param chain 필터 체인
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // Authorization 헤더에서 JWT 토큰 추출
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // "Bearer " 이후의 실제 토큰 값 추출
            String jwt = authorizationHeader.substring(7);
            try {
                // JWT에서 사용자 이름 추출
                String username = jwtUtil.extractUsername(jwt);

                // 현재 인증 컨텍스트가 비어 있는 경우에만 처리
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // 사용자 이름으로 사용자 정보를 로드
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                    // 토큰이 유효한지 확인
                    if (jwtUtil.validateToken(jwt)) {
                        // 인증 객체 생성 및 컨텍스트 설정
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            } catch (ExpiredJwtException e) {
                // 토큰이 만료된 경우 처리
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token has expired. Please log in again.");
                return; // 요청을 차단
            } catch (JwtException e) {
                // 잘못된 토큰 처리
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Invalid token.");
                return; // 요청을 차단
            } catch (RuntimeException e) {
                // 기타 예외 처리
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Unexpected error occurred.");
                return; // 요청을 차단
            }
        }

        // 필터 체인의 다음 필터로 요청을 전달
        chain.doFilter(request, response);
    }
}
