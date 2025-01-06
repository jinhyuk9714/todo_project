package com.example.todo.service;

import com.example.todo.entity.Todo;
import com.example.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> findByUserAndCompletionStatus(Long userId, boolean isCompleted) {
        try {
            return todoRepository.findByUserIdAndIsCompleted(userId, isCompleted);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving todos by status", e);
        }
    }
}
