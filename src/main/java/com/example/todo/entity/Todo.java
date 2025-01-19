package com.example.todo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Todo 엔티티 클래스.
 * 이 클래스는 데이터베이스의 "Todo" 테이블과 매핑된다.
 */
@Entity // JPA 엔티티임을 나타냄. 이 클래스는 데이터베이스 테이블과 매핑된다.
@Data // Lombok 어노테이션으로, getter, setter, toString, equals, hashCode 메서드를 자동으로 생성.
@Getter // 모든 필드에 대해 getter 메서드를 생성.
public class Todo {

    @Id // 이 필드가 테이블의 기본 키임을 나타냄.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 기본 키 생성 전략을 지정. `IDENTITY`는 데이터베이스가 자동으로 ID 값을 생성하도록 한다.
    private Long id;

    // Todo 항목의 내용. Not Null 제약 조건은 추가되지 않았음.
    private String task;

    // 완료 여부를 나타내는 필드. 기본값은 false로 설정.
    private Boolean isCompleted = false;

    // Todo 항목 생성 시간을 저장. 기본값은 현재 시간.
    private LocalDateTime createdAt = LocalDateTime.now();

    // 이 Todo 항목을 소유하는 사용자의 ID를 저장.
    private Long userId;

    // Lombok의 @Data 어노테이션을 사용하지만, 아래처럼 커스텀 getter/setter를 추가할 수도 있음.

    // Todo 항목 내용을 반환.
    public String getTask() {
        return task;
    }

    // Todo 항목 내용을 설정.
    public void setTask(String task) {
        this.task = task;
    }

    // 완료 여부를 반환.
    public Boolean getIsCompleted() {
        return isCompleted;
    }

    // 완료 여부를 설정.
    public void setIsCompleted(Boolean completed) {
        isCompleted = completed;
    }

    // ID 값을 반환.
    public Long getId() {
        return id;
    }

    // 생성 시간을 반환.
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // 소유자 사용자 ID를 반환.
    public Long getUserId() {
        return userId;
    }

    // 소유자 사용자 ID를 설정.
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}