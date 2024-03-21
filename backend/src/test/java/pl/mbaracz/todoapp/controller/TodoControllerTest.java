package pl.mbaracz.todoapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.mbaracz.todoapp.dto.TodoListDto;
import pl.mbaracz.todoapp.dto.TodoTaskDto;
import pl.mbaracz.todoapp.service.TodoService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoService todoService;

    private UUID existingTodoListId;

    @Before
    public void setUp() {
        List<TodoTaskDto> tasks = List.of(
                new TodoTaskDto("First task", false)
        );

        TodoListDto todoListDto = new TodoListDto(null, tasks, null, null);

        existingTodoListId = todoService.createTodoList(todoListDto);
    }

    @Test
    public void getNotExistingTodoList() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/api/todos/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getTodoList() throws Exception {
        mockMvc.perform(get("/api/todos/{id}", existingTodoListId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(existingTodoListId.toString()))
                .andExpect(jsonPath("$.tasks.length()").value(1))
                .andExpect(jsonPath("$.tasks[0].description").value("First task"))
                .andExpect(jsonPath("$.tasks[0].completed").value(false));
    }

    @Test
    public void createTodoList() throws Exception {
        List<TodoTaskDto> tasks = List.of(
                new TodoTaskDto("First task", false)
        );

        TodoListDto todoListDto = new TodoListDto(null, tasks, null, null);

        mockMvc.perform(put("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(todoListDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void updateNotExistingTodoList() throws Exception {
        List<TodoTaskDto> tasks = List.of(
                new TodoTaskDto("First task", false)
        );

        TodoListDto todoListDto = new TodoListDto(UUID.randomUUID(), tasks, null, null);

        mockMvc.perform(patch("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(todoListDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateTodoList() throws Exception {
        List<TodoTaskDto> tasks = List.of(
                new TodoTaskDto("First task updated", false)
        );

        TodoListDto todoListDto = new TodoListDto(existingTodoListId, tasks, null, null);

        mockMvc.perform(patch("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(todoListDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteNotExistingTodoList() throws Exception {
        mockMvc.perform(delete("/api/todos/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @AfterAll
    public void deleteTodoList() throws Exception {
        mockMvc.perform(delete("/api/todos/{id}", existingTodoListId))
                .andExpect(status().isOk());
    }

    private String asJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
