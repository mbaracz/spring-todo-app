package pl.mbaracz.todoapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mbaracz.todoapp.dto.TodoListDto;
import pl.mbaracz.todoapp.service.TodoService;

import java.util.UUID;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin("http://localhost:5173")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/{id}")
    public ResponseEntity<TodoListDto> findTodoListById(@PathVariable UUID id) {
        TodoListDto todoListDto = todoService.findTodoListById(id);

        if (todoListDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(todoListDto);
    }

    @PutMapping
    public ResponseEntity<UUID> createTodoList(@RequestBody TodoListDto todoListDto) {
        UUID todoListId = todoService.createTodoList(todoListDto);
        return ResponseEntity.ok(todoListId);
    }

    @PatchMapping
    public ResponseEntity<?> updateTodoList(@RequestBody TodoListDto todoListDto) {
        boolean updated = todoService.updateTodoList(todoListDto);

        if (updated) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodoList(@PathVariable UUID id) {
        boolean result = todoService.deleteTodoList(id);

        if (result) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
