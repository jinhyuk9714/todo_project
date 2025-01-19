package com.example.todo.config;

// Jackson과 Spring에서 필요한 클래스들 가져옴
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson의 ObjectMapper를 커스터마이징하는 설정 클래스.
 * 애플리케이션 전반에서 JSON 직렬화, 역직렬화 동작을 통일되게 만듦.
 */
@Configuration // 이 클래스를 스프링 설정 파일로 만듦
public class JacksonConfig {
    @Bean // 이 메서드의 반환 객체를 스프링 컨테이너에 Bean으로 등록
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper(); // ObjectMapper 객체 생성
        mapper.registerModule(new JavaTimeModule()); // Java 8 시간 관련 타입을 처리할 수 있도록 JavaTimeModule 추가
        return mapper; // 설정이 완료된 ObjectMapper 반환
    }
}
