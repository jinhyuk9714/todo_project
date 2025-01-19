package com.example.todo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Spring Boot의 기본 에러 처리를 커스터마이징하기 위한 컨트롤러.
 * 특정 HTTP 상태 코드에 따라 사용자가 정의한 에러 페이지로 포워딩.
 */
@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, HttpServletResponse response) {
        // 응답 상태 코드를 가져옴.
        int status = response.getStatus();

        // 상태 코드에 따라 다른 페이지로 포워딩.
        if (status == HttpServletResponse.SC_UNAUTHORIZED) {
            // 인증 실패 (401) 시 로그인 페이지로 포워딩
            return "forward:/frontend/pages/login.html";
        } else if (status == HttpServletResponse.SC_NOT_FOUND) {
            // 요청한 리소스를 찾을 수 없음 (404) 시 404 페이지로 포워딩
            return "forward:/frontend/pages/404.html";
        } else if (status == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            // 서버 내부 에러 (500) 시 500 페이지로 포워딩
            return "forward:/frontend/pages/500.html";
        }

        // 기타 에러는 기본 에러 페이지로 포워딩
        return "forward:/frontend/pages/error.html";
    }
}
