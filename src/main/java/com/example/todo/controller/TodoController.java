package com.example.todo.controller;

import com.example.todo.entity.Todo;
import com.example.todo.entity.User;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Todo 목록 관리 컨트롤러.
 * 사용자의 Todo CRUD 및 필터링, 완료 상태 토글, 완료 항목 일괄 삭제 기능을 처리.
 */
@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoRepository todoRepository; // Todo 데이터베이스 접근
    private final UserRepository userRepository; // User 데이터베이스 접근

    /**
     * TodoController 생성자.
     * TodoRepository와 UserRepository를 의존성 주입으로 초기화.
     */
    public TodoController(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    /**
     * 인증 객체에서 사용자 ID를 추출.
     * 인증 객체가 null이거나 사용자 이름이 없을 경우 예외를 발생시킴.
     */
    private Long getUserIdFromAuthentication(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Unauthorized");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        return user.getId();
    }

    /**
     * 사용자의 모든 Todo 항목 조회.
     */
    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos(Authentication authentication) {
        try {
            // 인증된 사용자의 ID 가져오기
            Long userId = getUserIdFromAuthentication(authentication);

            // 해당 사용자의 Todo 목록 조회
            List<Todo> todos = todoRepository.findByUserId(userId);

            // 조회된 Todo 목록을 반환
            return ResponseEntity.ok(todos);
        } catch (RuntimeException e) {
            // 인증 실패 시 403 상태 코드 반환
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (Exception e) {
            // 서버 오류 발생 시 500 상태 코드 반환
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 새로운 Todo 항목 생성.
     */
    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody Todo todo, Authentication authentication) {
        try {
            // 인증된 사용자 ID 가져오기
            Long userId = getUserIdFromAuthentication(authentication);

            // 사용자 ID 설정 및 저장
            todo.setUserId(userId);
            Todo savedTodo = todoRepository.save(todo);

            // 생성된 Todo 항목 반환
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTodo);
        } catch (Exception e) {
            // 서버 오류 발생 시 500 상태 코드 반환
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating Todo");
        }
    }

    /**
     * 특정 Todo 항목 수정.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo, Authentication authentication) {
        try {
            // 인증된 사용자 ID 가져오기
            Long userId = getUserIdFromAuthentication(authentication);

            // 수정하려는 Todo 항목 조회
            Todo todo = todoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Todo not found: " + id));

            // 요청한 사용자가 Todo 항목을 소유하고 있는지 확인
            if (!todo.getUserId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
            }

            // Todo 항목 업데이트
            todo.setTask(updatedTodo.getTask());
            todo.setIsCompleted(updatedTodo.getIsCompleted());
            Todo savedTodo = todoRepository.save(todo);

            return ResponseEntity.ok(savedTodo);
        } catch (Exception e) {
            // 서버 오류 발생 시 500 상태 코드 반환
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Todo 업데이트 중 오류가 발생했습니다.");
        }
    }

    /**
     * 특정 Todo 항목 삭제.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long id, Authentication authentication) {
        try {
            // 인증된 사용자 ID 가져오기
            Long userId = getUserIdFromAuthentication(authentication);

            // 삭제하려는 Todo 항목 조회
            Todo todo = todoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Todo not found: " + id));

            // 요청한 사용자가 Todo 항목을 소유하고 있는지 확인
            if (!todo.getUserId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Todo 항목 삭제
            todoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // 서버 오류 발생 시 500 상태 코드 반환
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting Todo");
        }
    }

    /**
     * 완료 상태에 따라 Todo 항목 필터링.
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Todo>> getTodosByStatus(@RequestParam boolean isCompleted, Authentication authentication) {
        try {
            // 인증된 사용자 ID 가져오기
            Long userId = getUserIdFromAuthentication(authentication);

            // 완료 상태에 따른 Todo 항목 조회
            List<Todo> todos = todoRepository.findByUserIdAndIsCompleted(userId, isCompleted);

            return ResponseEntity.ok(todos);
        } catch (Exception e) {
            // 서버 오류 발생 시 500 상태 코드 반환
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 특정 Todo 항목의 완료 상태 토글.
     */
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<?> toggleTodoCompletion(@PathVariable Long id, Authentication authentication) {
        try {
            // 인증된 사용자 ID 가져오기
            Long userId = getUserIdFromAuthentication(authentication);

            // 완료 상태를 토글하려는 Todo 항목 조회
            Todo todo = todoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Todo not found: " + id));

            // 요청한 사용자가 Todo 항목을 소유하고 있는지 확인
            if (!todo.getUserId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
            }

            // 완료 상태 토글
            todo.setIsCompleted(!todo.getIsCompleted());
            Todo updatedTodo = todoRepository.save(todo);

            return ResponseEntity.ok(updatedTodo);
        } catch (RuntimeException e) {
            // 인증 오류 발생 시 403 상태 코드 반환
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            // 서버 오류 발생 시 500 상태 코드 반환
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Todo 상태 변경 중 오류 발생");
        }
    }

    /**
     * 완료된 Todo 항목 모두 삭제.
     */
    @DeleteMapping("/completed")
    public ResponseEntity<?> deleteCompletedTodos(Authentication authentication) {
        try {
            // 인증된 사용자 ID 가져오기
            Long userId = getUserIdFromAuthentication(authentication);

            // 완료된 Todo 항목 조회 및 삭제
            List<Todo> completedTodos = todoRepository.findByUserIdAndIsCompleted(userId, true);
            todoRepository.deleteAll(completedTodos);

            return ResponseEntity.ok("완료된 Todo 항목이 삭제되었습니다.");
        } catch (RuntimeException e) {
            // 인증 오류 발생 시 403 상태 코드 반환
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            // 서버 오류 발생 시 500 상태 코드 반환
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("완료된 Todo 삭제 중 오류 발생");
        }
    }
}
