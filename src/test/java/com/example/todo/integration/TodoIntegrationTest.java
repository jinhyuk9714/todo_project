package com.example.todo.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFullWorkflow() throws Exception {
        // 1. Create a new Todo
        mockMvc.perform(post("/api/todos")
                        .contentType(APPLICATION_JSON)
                        .content("{\"task\": \"Integration Test Task\", \"isCompleted\": false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.task").value("Integration Test Task"));

        // 2. Get all Todos
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // 3. Update the Todo
        mockMvc.perform(put("/api/todos/1")
                        .contentType(APPLICATION_JSON)
                        .content("{\"task\": \"Updated Task\", \"isCompleted\": true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.task").value("Updated Task"));

        // 4. Delete the Todo
        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isOk());
    }
}
