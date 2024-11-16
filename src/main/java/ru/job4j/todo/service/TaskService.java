package ru.job4j.todo.service;


import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> findAll();
    Task addTask(Task task);
    Optional<Task> findById(int id);
    boolean update(Task task);
    boolean delete(int id);
}
