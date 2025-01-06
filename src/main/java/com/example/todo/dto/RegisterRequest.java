package com.example.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Username은 비워둘 수 없습니다.")
    @Size(min = 3, max = 20, message = "Username은 3자 이상, 20자 이하이어야 합니다.")
    private String username;

    @NotBlank(message = "Password는 비워둘 수 없습니다.")
    @Size(min = 6, message = "Password는 최소 6자 이상이어야 합니다.")
    private String password;

    // 기본 생성자
    public RegisterRequest() {
    }

    // 생성자
    public RegisterRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter와 Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
