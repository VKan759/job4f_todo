package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.TaskStore;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleTaskService implements TaskService {
    private final TaskStore taskStore;

    @Override
    public List<Task> findAll() {
        return taskStore.findAll();
    }

    @Override
    public Optional<Task> addTask(Task task, List<Integer> categoryIds) {
        return taskStore.addTask(task, categoryIds);
    }

    @Override
    public Optional<Task> findById(int id) {
        return taskStore.findById(id);
    }

    @Override
    public boolean update(Task task, List<Integer> categoryIds) {
        return taskStore.update(task, categoryIds);
    }

    @Override
    public boolean delete(int id) {
        return taskStore.delete(id);
    }

    @Override
    public List<Task> findTaskByStatus(boolean taskStatus) {
        return taskStore.findTasksByStatus(taskStatus);
    }

    @Override
    public boolean setDoneById(int id) {
        return taskStore.setDoneById(id);
    }
}
