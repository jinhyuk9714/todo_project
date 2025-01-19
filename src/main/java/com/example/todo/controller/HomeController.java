package com.example.todo.controller;

import com.example.todo.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 애플리케이션의 루트 URL("/")에 대한 요청을 처리하는 컨트롤러.
 * 클라이언트가 로그인 여부를 확인하고 적절한 페이지로 이동하도록 리다이렉트 처리.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String handleRootRequest() {
        // 클라이언트가 초기 화면(index.html)으로 리다이렉트되도록 설정
        return "redirect:/frontend/index.html";
    }
}
