package com.example.todo.integration;

import com.example.todo.entity.Todo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // 테스트 클래스에 추가하여 모든 테스트를 트랜잭션으로 실행
public class TodoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void clearDatabase() {
        entityManager.createQuery("DELETE FROM Todo").executeUpdate(); // 트랜잭션은 자동 관리됨
    }

    @Test
    public void testFullWorkflow() throws Exception {
        Todo newTodo = new Todo();
        newTodo.setTask("Integration Test Task");
        newTodo.setIsCompleted(false);

        // 1. Create a new Todo
        String response = mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newTodo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.task").value("Integration Test Task"))
                .andExpect(jsonPath("$.isCompleted").value(false))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract the ID from the response
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Todo createdTodo = objectMapper.readValue(response, Todo.class);
        Long createdTodoId = createdTodo.getId();

        // 2. Get all Todos
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1)); // 배열 크기 확인

        // 3. Update the Todo
        mockMvc.perform(put("/api/todos/" + createdTodoId)
                        .contentType(APPLICATION_JSON)
                        .content("{\"task\": \"Updated Task\", \"isCompleted\": true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.task").value("Updated Task"));

        // 4. Delete the Todo
        mockMvc.perform(delete("/api/todos/" + createdTodoId))
                .andExpect(status().isNoContent());

        // 5. Check if Todo is deleted
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0)); // 데이터가 없음을 확인
    }


    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // LocalDateTime 지원 추가
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
