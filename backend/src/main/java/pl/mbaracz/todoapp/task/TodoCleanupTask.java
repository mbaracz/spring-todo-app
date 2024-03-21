package pl.mbaracz.todoapp.task;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.mbaracz.todoapp.model.TodoList;
import pl.mbaracz.todoapp.repository.TodoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoCleanupTask {

    private final TodoRepository todoRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void cleanupOldTodoLists() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

        List<TodoList> oldTodoLists = todoRepository.findAllByUpdatedAtBefore(thirtyDaysAgo);

        todoRepository.deleteAll(oldTodoLists);
    }
}
