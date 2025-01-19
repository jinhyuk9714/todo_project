package com.example.todo.repository;

import com.example.todo.entity.User; // User 엔티티 클래스 임포트
import org.springframework.data.jpa.repository.JpaRepository; // JpaRepository 인터페이스 임포트
import org.springframework.stereotype.Repository; // Repository 어노테이션 임포트

import java.util.Optional;

/**
 * UserRepository 인터페이스.
 * User 엔티티와 데이터베이스 간의 작업을 처리한다.
 */
@Repository // 이 인터페이스를 Spring에서 데이터 액세스 계층으로 인식하도록 설정
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 사용자 이름으로 User 엔티티를 조회.
     * @param username 검색할 사용자 이름
     * @return 사용자 이름에 해당하는 User 엔티티를 Optional로 반환
     */
    Optional<User> findByUsername(String username);

    /**
     * 특정 사용자 이름이 데이터베이스에 존재하는지 확인.
     * @param username 확인할 사용자 이름
     * @return 사용자 이름이 존재하면 true, 그렇지 않으면 false
     */
    boolean existsByUsername(String username);
}
