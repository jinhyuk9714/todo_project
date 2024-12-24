package com.example.todo.controller;

import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
import com.example.todo.service.TodoService;
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

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return todoRepository.save(todo);
    }

    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo) {
        Todo todo = todoRepository.findById(id).orElseThrow();
        todo.setTask(updatedTodo.getTask());
        todo.setIsCompleted(updatedTodo.getIsCompleted());
        return todoRepository.save(todo);
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable Long id) {
        todoRepository.deleteById(id);
    }

    @PatchMapping("/{id}/toggle")
    public Todo toggleTodo(@PathVariable Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new RuntimeException("Todo not found"));
        todo.setIsCompleted(!todo.getIsCompleted()); // 현재 상태 반전
        return todoRepository.save(todo);
    }

    @GetMapping("/filter")
    public List<Todo> getTodosByStatus(@RequestParam("isCompleted") boolean isCompleted) {
        return todoService.findByCompletionStatus(isCompleted);
    }


    @DeleteMapping("/completed")
    public void deleteCompletedTodos() {
        List<Todo> completedTodos = todoRepository.findByCompletionStatus(true);
        todoRepository.deleteAll(completedTodos);
    }

}
