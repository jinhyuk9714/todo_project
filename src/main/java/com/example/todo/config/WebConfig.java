package com.example.todo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS(Cross-Origin Resource Sharing) 설정을 위한 클래스.
 * 클라이언트와 서버가 다른 도메인일 때 HTTP 요청이 허용되도록 설정.
 */
@Configuration // 스프링 설정 클래스로 지정
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해 CORS 허용
                .allowedOrigins(
                        "http://localhost:3000",
                        "http://localhost:8080",
                        "https://todo-project-j3jq.onrender.com"
                ) // 허용할 도메인 목록
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 모든 요청 헤더 허용
                .allowCredentials(true); // 쿠키 및 인증 정보를 포함한 요청 허용
    }
}
