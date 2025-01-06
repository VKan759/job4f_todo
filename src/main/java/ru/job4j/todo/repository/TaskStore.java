package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class TaskStore {
    private final CrudRepository crudRepository;

    public List<Task> findAll() {
        return crudRepository.query("""
                FROM Task t 
                JOIN FETCH t.priority 
                LEFT JOIN FETCH t.categories
                """, Task.class).stream().distinct().toList();
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

    public List<Task> findTasksByStatus(boolean taskStatus, User user) {
        List<Task> tasks = crudRepository.query("""
                        from Task t 
                        join fetch t.priority 
                        left join fetch t.categories 
                        where t.done = :fDone
                        """,
                Task.class, Map.of(
                        "fDone",
                        taskStatus)).stream().distinct().toList();
        for (Task task : tasks) {
            task.setCreated(
                    task.getCreated().withZoneSameInstant(ZoneId.of(user.getTimezone())));
        }
        return tasks;
    }

    public Optional<Task> findById(int id) {
        return crudRepository.optional("""
                        from Task t 
                        join fetch t.priority 
                        left join fetch t.categories
                        where t.id = :fId""", Task.class,
                Map.of("fId",
                        id));
    }

    public boolean update(Task task, List<Integer> categoryIds) {
        crudRepository.makeChangesNativeQuery("delete from participates where task_id = :fTaskId", Map.of("fTaskId",
                task.getId()));
        categoryIds.forEach(categoryId ->
                crudRepository.makeChangesNativeQuery("""
                        insert into participates (task_id, category_id) values 
                        (:taskId, :categoryId)""", Map.of("taskId", task.getId(),
                        "categoryId", categoryId)
                ));
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