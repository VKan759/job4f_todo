package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class TaskStore {
    private final SessionFactory sf;

    public List<Task> findAll() {
        List<Task> result = List.of();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            result = session.createQuery("from Task").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    public Task addTask(Task task) {
        Session session = sf.openSession();
        Task result = null;
        try {
            session.beginTransaction();
            Serializable saved = session.save(task);
            task.setId((Integer) saved);
            result = task;
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    public Optional<Task> findById(int id) {
        Session session = sf.openSession();
        Task result = null;
        try {
            session.beginTransaction();
            result = session.createQuery("from Task where id = :fId", Task.class)
                    .setParameter("fId", id).uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return Optional.ofNullable(result);
    }

    public boolean update(Task task) {
        Session session = sf.openSession();
        int changedRows = 0;
        try {
            session.beginTransaction();
            changedRows = session.createQuery("""
                            update Task set description = :fDescription, created = :fCreated, done = :fDone, full_description = :fullDescription where id  = :fId
                            """)
                    .setParameter("fDescription", task.getDescription())
                    .setParameter("fCreated", task.getCreated())
                    .setParameter("fDone", task.isDone())
                    .setParameter("fId", task.getId())
                    .setParameter("fullDescription", task.getFullDescription())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return changedRows > 0;
    }

    public boolean delete(int id) {
        Session session = sf.openSession();
        int deletedRows = 0;
        try {
            session.beginTransaction();
            deletedRows = session.createQuery("delete Task where id = :fId")
                    .setParameter("fId", id)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return deletedRows > 0;
    }
}