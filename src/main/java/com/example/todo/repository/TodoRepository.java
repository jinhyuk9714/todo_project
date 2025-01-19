package com.example.todo.repository;

import com.example.todo.entity.Todo; // Todo 엔티티 클래스 임포트
import org.springframework.data.jpa.repository.JpaRepository; // JpaRepository 인터페이스 임포트
import org.springframework.stereotype.Repository; // Repository 어노테이션 임포트

import java.util.List;

/**
 * TodoRepository 인터페이스.
 * Spring Data JPA를 사용하여 Todo 엔티티와 데이터베이스 간의 작업을 처리한다.
 */
@Repository // 이 인터페이스가 Spring에서 관리되는 데이터 액세스 계층임을 명시
public interface TodoRepository extends JpaRepository<Todo, Long> {

    /**
     * 특정 사용자 ID로 Todo 항목 조회.
     * @param userId 사용자 ID
     * @return 사용자 ID에 해당하는 Todo 리스트
     */
    List<Todo> findByUserId(Long userId);

    /**
     * 특정 사용자 ID와 완료 여부로 Todo 항목 조회.
     * @param userId 사용자 ID
     * @param isCompleted 완료 여부 (true: 완료, false: 미완료)
     * @return 조건에 맞는 Todo 리스트
     */
    List<Todo> findByUserIdAndIsCompleted(Long userId, boolean isCompleted);
}
