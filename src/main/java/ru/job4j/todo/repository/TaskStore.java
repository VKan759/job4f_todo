package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class TaskStore {
    private final CrudRepository crudRepository;

    public List<Task> findAll() {
        return crudRepository.query("from Task t join fetch t.priority", Task.class);
    }

    public Optional<Task> addTask(Task task) {
        try {
            crudRepository.run(session -> session.persist(task));
            return Optional.of(task);
        } catch (Exception e) {
            log.error("Не удалось добавить задачу");
        }
        return Optional.empty();
    }

    public List<Task> findTasksByStatus(boolean taskStatus) {
        return crudRepository.query("from Task task join fetch task.priority where done = :fDone", Task.class, Map.of(
                "fDone",
                taskStatus));
    }

    public Optional<Task> findById(int id) {
        return crudRepository.optional("from Task task join fetch task.priority where task.id = :fId", Task.class,
                Map.of("fId",
                id));
    }

    public boolean update(Task task) {
        return crudRepository.makeChanges("""
                update Task set description = :fDescription,
                title = :fTitle,
                priority = :fPriority
                where id = :fId
                """,
                Map.of("fDescription", task.getDescription(),
                        "fId", task.getId(),
                        "fTitle", task.getTitle(),
                        "fPriority", task.getPriority()
                ));
    }

    public boolean setDoneById(int id) {
        return crudRepository.makeChanges("update Task set done = true where id = :fId", Map.of("fId", id));
    }

    public boolean delete(int id) {
        return crudRepository.makeChanges("delete Task where id = :fId", Map.of("fId", id));
    }
}