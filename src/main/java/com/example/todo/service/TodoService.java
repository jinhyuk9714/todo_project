package com.example.todo.service;

import com.example.todo.entity.Todo;
import com.example.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    // Todo 데이터를 관리하는 레포지토리. 데이터베이스와의 상호작용 담당.
    private final TodoRepository todoRepository;

    // 생성자 주입을 통해 TodoRepository를 초기화.
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    /**
     * 주어진 사용자 ID와 완료 상태에 따라 Todo 항목을 조회.
     *
     * @param userId 사용자의 고유 ID
     * @param isCompleted 완료 상태 (true: 완료된 항목, false: 미완료 항목)
     * @return 필터링된 Todo 항목 리스트
     */
    public List<Todo> findByUserAndCompletionStatus(Long userId, boolean isCompleted) {
        try {
            // 사용자 ID와 완료 상태를 기준으로 Todo 항목 조회.
            return todoRepository.findByUserIdAndIsCompleted(userId, isCompleted);
        } catch (Exception e) {
            // 예외 발생 시 런타임 예외로 래핑하여 호출자에게 전달.
            throw new RuntimeException("Error retrieving todos by status", e);
        }
    }
}

