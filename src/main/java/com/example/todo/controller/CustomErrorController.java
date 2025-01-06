package com.example.todo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, HttpServletResponse response) {
        int status = response.getStatus();

        // 상태 코드에 따라 처리
        if (status == HttpServletResponse.SC_UNAUTHORIZED) {
            return "forward:/frontend/pages/login.html"; // 인증 실패 시 로그인 페이지로 이동
        } else if (status == HttpServletResponse.SC_NOT_FOUND) {
            return "forward:/frontend/pages/404.html"; // 404 에러 페이지로 이동
        } else if (status == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            return "forward:/frontend/pages/500.html"; // 500 에러 페이지로 이동
        }

        return "forward:/frontend/pages/error.html"; // 기타 에러 처리
    }
}
