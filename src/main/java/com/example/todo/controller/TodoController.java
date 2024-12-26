package com.example.todo.controller;

import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
import com.example.todo.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoRepository todoRepository;
    private final TodoService todoService;

    public TodoController(TodoRepository todoRepository, TodoService todoService) {
        this.todoRepository = todoRepository;
        this.todoService = todoService;
    }

    // 모든 Todo 조회
    @GetMapping
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    // 새로운 Todo 생성
    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        Todo savedTodo = todoRepository.save(todo);
        return new ResponseEntity<>(savedTodo, HttpStatus.CREATED);
    }

    // 특정 Todo 수정
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo를 찾을 수 없습니다: " + id));
        todo.setTask(updatedTodo.getTask());
        todo.setIsCompleted(updatedTodo.getIsCompleted());
        Todo savedTodo = todoRepository.save(todo);
        return ResponseEntity.ok(savedTodo);
    }

    // 특정 Todo 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        if (!todoRepository.existsById(id)) {
            throw new RuntimeException("Todo를 찾을 수 없습니다: " + id);
        }
        todoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Todo 완료 상태 토글
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Todo> toggleTodo(@PathVariable Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo를 찾을 수 없습니다: " + id));
        todo.setIsCompleted(!todo.getIsCompleted());
        Todo updatedTodo = todoRepository.save(todo);
        return ResponseEntity.ok(updatedTodo);
    }

    // 완료 상태에 따라 Todo 필터링
    @GetMapping("/filter")
    public ResponseEntity<List<Todo>> getTodosByStatus(@RequestParam("isCompleted") boolean isCompleted) {
        List<Todo> todos = todoService.findByCompletionStatus(isCompleted);
        return ResponseEntity.ok(todos);
    }

    // 완료된 Todo 일괄 삭제
    @DeleteMapping("/completed")
    public ResponseEntity<Void> deleteCompletedTodos() {
        List<Todo> completedTodos = todoRepository.findByCompletionStatus(true);
        todoRepository.deleteAll(completedTodos);
        return ResponseEntity.noContent().build();
    }
}
