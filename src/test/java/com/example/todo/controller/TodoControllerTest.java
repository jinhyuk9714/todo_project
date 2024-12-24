package com.example.todo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllTodos() throws Exception {
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    public void testCreateTodo() throws Exception {
        mockMvc.perform(post("/api/todos")
                        .contentType(APPLICATION_JSON)
                        .content("{\"task\": \"Test Task\", \"isCompleted\": false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.task").value("Test Task"));
    }
}
