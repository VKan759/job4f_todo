package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class TaskStore {
    private final CrudRepository crudRepository;

    public List<Task> findAll() {
        return crudRepository.query("from Task", Task.class);
    }

    public Optional<Task> addTask(Task task) {
        try {
            crudRepository.run(session -> session.persist(task));
            return Optional.of(task);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Task> findTasksByStatus(boolean taskStatus) {
        return crudRepository.query("from Task where done = :fDone", Task.class, Map.of("fDone", taskStatus));
    }

    public Optional<Task> findById(int id) {
        return crudRepository.optional("from Task where id = :fId", Task.class, Map.of("fId", id));
    }

    public boolean update(Task task) {
        Task mergedTask = (Task) crudRepository.tx(session -> session.merge(task));
        return !task.equals(mergedTask);
    }

    public boolean setDoneById(int id) {
        return crudRepository.makeChanges("update Task set done = true where id = :fId", Map.of("fId", id));
    }

    public boolean delete(int id) {
        return crudRepository.makeChanges("delete Task where id = :fId", Map.of("fId", id));
    }
}