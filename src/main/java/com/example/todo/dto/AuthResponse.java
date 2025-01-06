package com.example.todo.dto;

public class AuthResponse {

    private String token;

    // 기본 생성자
    public AuthResponse() {}

    // 매개변수 있는 생성자
    public AuthResponse(String token) {
        this.token = token;
    }

    // Getter 및 Setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
