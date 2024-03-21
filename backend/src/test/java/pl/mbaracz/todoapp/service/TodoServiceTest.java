package pl.mbaracz.todoapp.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.mbaracz.todoapp.dto.TodoListDto;
import pl.mbaracz.todoapp.dto.TodoTaskDto;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TodoServiceTest {

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
    public void findTodoListByNotExistingId() {
        TodoListDto todoListDto = todoService.findTodoListById(UUID.randomUUID());
        assertNull(todoListDto);
    }

    @Test
    public void findTodoListById() {
        TodoListDto retrieved = todoService.findTodoListById(existingTodoListId);

        assertNotNull(retrieved.id());
        assertNotNull(retrieved.createdAt());
        assertNotNull(retrieved.updatedAt());
    }

    @Test
    public void createTodoList() {
        List<TodoTaskDto> tasks = List.of(
                new TodoTaskDto("First task", false)
        );
        TodoListDto todoListDto = new TodoListDto(null, tasks, null, null);

        UUID id = todoService.createTodoList(todoListDto);

        assertNotNull(id);
        TodoTaskDto task = todoListDto.tasks().get(0);
        TodoTaskDto expectedTask = tasks.get(0);
        assertEquals(expectedTask.completed(), task.completed());
        assertEquals(expectedTask.description(), task.description());
    }

    @Test
    public void updateNotExistingTodoList() {
        List<TodoTaskDto> tasks = List.of(
                new TodoTaskDto("First task", true)
        );
        TodoListDto todoListDto = new TodoListDto(UUID.randomUUID(), tasks, null, null);

        boolean updated = todoService.updateTodoList(todoListDto);

        assertFalse(updated);
    }

    @Test
    public void updateTodoList() {
        List<TodoTaskDto> tasks = List.of(
                new TodoTaskDto("Updated task", true)
        );
        TodoListDto todoListDto = new TodoListDto(existingTodoListId, tasks, null, null);

        boolean updated = todoService.updateTodoList(todoListDto);

        assertTrue(updated);
    }
}
