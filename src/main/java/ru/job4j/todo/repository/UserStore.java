package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserStore implements UserRepository {
    private final CrudRepository crudRepository;

    @Override
    public Optional<User> save(User user) {
        try {
            crudRepository.run(session -> session.persist(user));
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return crudRepository.optional("from User where email = :fEmail and password = :fPassword", User.class, Map.of(
                "fEmail",
                email,
                "fPassword", password));
    }
}
