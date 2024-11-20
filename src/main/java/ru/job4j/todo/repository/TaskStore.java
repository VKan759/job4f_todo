package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

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
            result = session.createQuery("from Task", Task.class).list();
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
            session.save(task);
            result = task;
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    public List<Task> findTasksByStatus(boolean taskStatus) {
        Session session = sf.openSession();
        List<Task> result = List.of();
        try {
            session.beginTransaction();
            result = session.createQuery("from Task where done = :fStatus", Task.class)
                    .setParameter("fStatus", taskStatus).list();
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
        Optional<Task> result = Optional.empty();
        try {
            session.beginTransaction();
            result = session.createQuery("from Task where id = :fId", Task.class)
                    .setParameter("fId", id).uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    public boolean update(Task task) {
        Session session = sf.openSession();
        int changedRows = 0;
        try {
            session.beginTransaction();
            changedRows = session.createQuery("""
                            update Task set title = :fTitle, created = :fCreated, done = :fDone, description = :fDescription where id  = :fId
                            """)
                    .setParameter("fTitle", task.getTitle())
                    .setParameter("fCreated", task.getCreated())
                    .setParameter("fDone", task.isDone())
                    .setParameter("fId", task.getId())
                    .setParameter("fDescription", task.getDescription())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return changedRows > 0;
    }

    public boolean setDoneById(int id) {
        Session session = sf.openSession();
        int setDoneTasks = 0;
        try {
            session.beginTransaction();
            setDoneTasks = session.createQuery("update Task set done = true where id = :fId")
                    .setParameter("fId", id).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return setDoneTasks > 0;
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