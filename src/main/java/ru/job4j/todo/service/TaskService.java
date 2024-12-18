package ru.job4j.todo.service;


import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> findAll(User user);

    Optional<Task> addTask(Task task, List<Integer> categoryIds);

    Optional<Task> findById(int id);

    boolean update(Task task, List<Integer> categoryIds);

    boolean delete(int id);

    List<Task> findTaskByStatus(boolean taskStatus, User user);

    boolean setDoneById(int id);
}
