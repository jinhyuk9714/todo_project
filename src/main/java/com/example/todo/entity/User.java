package com.example.todo.entity;

import jakarta.persistence.*; // JPA 관련 어노테이션
import lombok.Getter; // Lombok의 @Getter를 통해 getter 메서드 자동 생성
import lombok.Setter; // Lombok의 @Setter를 통해 setter 메서드 자동 생성

/**
 * User 엔티티 클래스.
 * 이 클래스는 "users" 테이블과 매핑되어 사용자 정보를 저장한다.
 */
@Entity // JPA 엔티티임을 나타냄. 데이터베이스의 테이블과 매핑된다.
@Getter // Lombok 어노테이션으로 모든 필드의 getter 메서드를 자동 생성한다.
@Setter // Lombok 어노테이션으로 모든 필드의 setter 메서드를 자동 생성한다.
@Table(name = "users") // "users"라는 테이블 이름을 지정. PostgreSQL 예약어 충돌 방지.
public class User {

    @Id // 이 필드가 기본 키임을 나타냄.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 기본 키 생성을 데이터베이스에 위임. (PostgreSQL의 SERIAL 또는 IDENTITY 사용)
    private Long id;

    @Column(unique = true, nullable = false)
    // "username" 컬럼은 고유(unique)해야 하며, NULL 값을 허용하지 않음.
    private String username;

    @Column(nullable = false)
    // "password" 컬럼은 NULL 값을 허용하지 않음.
    private String password;

    // 사용자 역할을 저장. 기본값으로 "USER"를 설정.
    private String role = "USER";

    // Lombok의 @Getter와 @Setter가 메서드를 생성하지만, 필요하면 아래와 같이 커스텀 구현 가능.

    // 사용자 ID를 반환.
    public Long getId() {
        return id;
    }

    // 사용자 ID를 설정.
    public void setId(Long id) {
        this.id = id;
    }

    // 사용자 이름을 반환.
    public String getUsername() {
        return username;
    }

    // 사용자 이름을 설정.
    public void setUsername(String username) {
        this.username = username;
    }

    // 비밀번호를 반환.
    public String getPassword() {
        return password;
    }

    // 비밀번호를 설정.
    public void setPassword(String password) {
        this.password = password;
    }

    // 사용자 역할을 반환.
    public String getRole() {
        return role;
    }

    // 사용자 역할을 설정.
    public void setRole(String role) {
        this.role = role;
    }
}
