package pl.mbaracz.todoapp.dto;

import pl.mbaracz.todoapp.model.TodoList;
import pl.mbaracz.todoapp.model.TodoTask;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record TodoListDto(
        UUID id,
        List<TodoTaskDto> tasks,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static TodoListDto toDto(TodoList todoList) {
        List<TodoTaskDto> tasks = todoList.getTasks()
                .stream()
                .map(TodoTaskDto::toDto)
                .toList();

        return new TodoListDto(
                todoList.getId(),
                tasks,
                todoList.getCreatedAt(),
                todoList.getUpdatedAt()
        );
    }

    public static TodoList fromDto(TodoListDto todoListDto) {
        TodoList todoList = new TodoList(
                null,
                null,
                null,
                new ArrayList<>()
        );

        List<TodoTask> tasks = todoListDto.tasks()
                .stream()
                .map(TodoTaskDto::fromDto)
                .peek(todoTask -> todoTask.setTodoList(todoList))
                .toList();

        todoList.addTasks(tasks);

        return todoList;
    }
}
