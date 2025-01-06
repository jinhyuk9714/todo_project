package com.example.todo.controller;

import com.example.todo.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String handleRootRequest() {
        // 클라이언트가 로그인 여부를 확인하도록 index.html로 리다이렉트
        return "redirect:/frontend/index.html";
    }
}
