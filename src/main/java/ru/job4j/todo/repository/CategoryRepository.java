package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class CategoryRepository {
    private final CrudRepository repository;

    public List<Category> findAll() {
        return repository.query("from Category", Category.class);
    }

    public Optional<Category> findById(int id) {
        return repository.optional("from Category c where c.id = :fId", Category.class, Map.of("fId", id));
    }

    public List<Category> findAllWhenIds(List<Integer> ids) {
        return repository.query("from Category c where c.id in :fIds", Category.class, Map.of("fIds", ids));
    }
}
