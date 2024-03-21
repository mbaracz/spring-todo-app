package pl.mbaracz.todoapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mbaracz.todoapp.dto.TodoListDto;
import pl.mbaracz.todoapp.dto.TodoTaskDto;
import pl.mbaracz.todoapp.model.TodoList;
import pl.mbaracz.todoapp.model.TodoTask;
import pl.mbaracz.todoapp.repository.TodoRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoListDto findTodoListById(UUID uniqueId) {
        TodoList todoList = todoRepository.findById(uniqueId).orElse(null);

        if (todoList == null) {
            return null;
        }

        return TodoListDto.toDto(todoList);
    }

    public UUID createTodoList(TodoListDto todoListDto) {
        if (todoListDto.tasks() == null || todoListDto.tasks().isEmpty()) {
            return null;
        }
        TodoList todoList = TodoListDto.fromDto(todoListDto);
        todoList = todoRepository.save(todoList);
        return todoList.getId();
    }

    public boolean updateTodoList(TodoListDto todoListDto) {
        if (todoListDto.id() == null || todoListDto.tasks() == null) {
            return false;
        }

        TodoList todoList = todoRepository.findById(todoListDto.id()).orElse(null);

        if (todoList == null) {
            return false;
        }

        List<TodoTask> taskList = todoListDto.tasks()
                .stream()
                .map(TodoTaskDto::fromDto)
                .peek(todoTask -> todoTask.setTodoList(todoList))
                .toList();

        todoList.clearTasks();

        todoList.addTasks(taskList);

        todoRepository.save(todoList);

        return true;
    }

    public boolean deleteTodoList(UUID id) {
        boolean exists = todoRepository.existsById(id);

        if (exists) {
            todoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
