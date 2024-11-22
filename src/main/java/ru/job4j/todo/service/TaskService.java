package ru.job4j.todo.service;


import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> findAll();

    Optional<Task> addTask(Task task);

    Optional<Task> findById(int id);

    boolean update(Task task, User user);

    boolean delete(int id);

    List<Task> findTaskByStatus(boolean taskStatus);

    boolean setDoneById(int id);
}
