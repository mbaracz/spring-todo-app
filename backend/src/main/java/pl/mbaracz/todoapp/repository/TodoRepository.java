package pl.mbaracz.todoapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mbaracz.todoapp.model.TodoList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TodoRepository extends CrudRepository<TodoList, UUID> {

    List<TodoList> findAllByUpdatedAtBefore(LocalDateTime localDateTime);

}
