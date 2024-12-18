package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Repository
@AllArgsConstructor
public class CrudRepository {
    private final SessionFactory sf;

    public <T> T tx(Function<Session, T> command) {
        Session session = sf.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            T apply = command.apply(session);
            transaction.commit();
            return apply;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public boolean makeChanges(String query, Map<String, Object> map) {
        Function<Session, Integer> command = session -> {
            var sessionQuery = session.createQuery(query);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                sessionQuery.setParameter(entry.getKey(), entry.getValue());
            }
            return sessionQuery.executeUpdate();
        };
        return tx(command) > 0;
    }

    public boolean makeChangesNativeQuery(String query, Map<String, Object> map) {
        Function<Session, Integer> command = session -> {
            var sessionQuery = session.createNativeQuery(query);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                sessionQuery.setParameter(entry.getKey(), entry.getValue());
            }
            return sessionQuery.executeUpdate();
        };
        return tx(command) > 0;
    }

    public void run(Consumer<Session> command) {
        tx(session -> {
            command.accept(session);
            return null;
        });
    }

    public void run(String query, Map<String, Object> map) {
        Consumer<Session> command = session -> {
            var sessionQuery = session.createQuery(query);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                sessionQuery.setParameter(entry.getKey(), entry.getValue());
            }
            sessionQuery.executeUpdate();
        };
        run(command);
    }

    public <T> Optional<T> optional(String query, Class<T> tClass, Map<String, Object> args) {
        Function<Session, Optional<T>> command = session -> {
            Query<T> sessionQuery = session.createQuery(query, tClass);
            for (Map.Entry<String, Object> entry : args.entrySet()) {
                sessionQuery.setParameter(entry.getKey(), entry.getValue());
            }
            return sessionQuery.uniqueResultOptional();
        };
        return tx(command);
    }

    public <T> List<T> query(String query, Class<T> tClass) {
        Function<Session, List<T>> command = session ->
                session.createQuery(query, tClass).list();
        return tx(command);
    }

    public <T> List<T> query(String query, Class<T> tClass, Map<String, Object> args) {
        Function<Session, List<T>> command = session -> {
            Query<T> sessionQuery = session.createQuery(query, tClass);
            for (Map.Entry<String, Object> entry : args.entrySet()) {
                sessionQuery.setParameter(entry.getKey(), entry.getValue());
            }
            return sessionQuery.list();
        };
        return tx(command);
    }
}
