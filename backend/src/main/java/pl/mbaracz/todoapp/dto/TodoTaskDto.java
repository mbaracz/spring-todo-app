package pl.mbaracz.todoapp.dto;

import pl.mbaracz.todoapp.model.TodoTask;

public record TodoTaskDto(
        String description,
        boolean completed
) {
    public static TodoTaskDto toDto(TodoTask todoTask) {
        return new TodoTaskDto(
                todoTask.getDescription(),
                todoTask.isCompleted()
        );
    }

    public static TodoTask fromDto(TodoTaskDto todoTaskDto) {
        return new TodoTask(
                null,
                todoTaskDto.description(),
                todoTaskDto.completed(),
                null
        );
    }
}
