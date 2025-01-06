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

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoController(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Unauthorized");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        return user.getId();
    }

    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos(Authentication authentication) {
        try {
            Long userId = getUserIdFromAuthentication(authentication);
            List<Todo> todos = todoRepository.findByUserId(userId);
            return ResponseEntity.ok(todos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody Todo todo, Authentication authentication) {
        try {
            Long userId = getUserIdFromAuthentication(authentication);
            todo.setUserId(userId);
            Todo savedTodo = todoRepository.save(todo);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTodo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating Todo");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo, Authentication authentication) {
        try {
            // 인증 정보 확인
            if (authentication == null || authentication.getName() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 사용자입니다.");
            }

            // 사용자 ID 가져오기
            Long userId = getUserIdFromAuthentication(authentication);

            // 수정하려는 Todo가 존재하는지 확인
            Todo todo = todoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("해당 Todo를 찾을 수 없습니다: " + id));

            // 요청한 사용자가 Todo를 소유하고 있는지 확인
            if (!todo.getUserId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
            }

            // Todo 업데이트
            todo.setTask(updatedTodo.getTask());
            todo.setIsCompleted(updatedTodo.getIsCompleted());
            Todo savedTodo = todoRepository.save(todo);
            return ResponseEntity.ok(savedTodo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Todo 업데이트 중 오류가 발생했습니다.");
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long id, Authentication authentication) {
        try {
            Long userId = getUserIdFromAuthentication(authentication);
            Todo todo = todoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Todo not found: " + id));

            if (!todo.getUserId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            todoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting Todo");
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Todo>> getTodosByStatus(@RequestParam boolean isCompleted, Authentication authentication) {
        try {
            Long userId = getUserIdFromAuthentication(authentication);
            List<Todo> todos = todoRepository.findByUserIdAndIsCompleted(userId, isCompleted);
            return ResponseEntity.ok(todos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<?> toggleTodoCompletion(@PathVariable Long id, Authentication authentication) {
        try {
            // 인증된 사용자 확인
            Long userId = getUserIdFromAuthentication(authentication);

            // Todo 찾기
            Todo todo = todoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Todo not found: " + id));

            // Todo 소유권 확인
            if (!todo.getUserId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
            }

            // 완료 상태 토글
            todo.setIsCompleted(!todo.getIsCompleted());
            Todo updatedTodo = todoRepository.save(todo);

            return ResponseEntity.ok(updatedTodo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Todo 상태 변경 중 오류 발생");
        }
    }
    @DeleteMapping("/completed")
    public ResponseEntity<?> deleteCompletedTodos(Authentication authentication) {
        try {
            // 인증된 사용자 확인
            Long userId = getUserIdFromAuthentication(authentication);

            // 사용자 ID와 완료 상태를 기준으로 삭제
            List<Todo> completedTodos = todoRepository.findByUserIdAndIsCompleted(userId, true);
            todoRepository.deleteAll(completedTodos);

            return ResponseEntity.ok("완료된 Todo 항목이 삭제되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("완료된 Todo 삭제 중 오류 발생");
        }
    }

}

