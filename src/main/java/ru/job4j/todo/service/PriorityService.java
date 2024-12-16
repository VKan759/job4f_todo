package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.repository.PriorityRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class PriorityService {
    private final PriorityRepository repository;

    public List<Priority> findAll() {
        return repository.findAll();
    }

    public Optional<Priority> findById(int id) {
        return repository.findById(id);
    }
}
