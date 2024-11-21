ALTER TABLE tasks add column user_id int references todo_user(id);

INSERT INTO tasks (title, created, done, user_id)
VALUES
    ('Задача 6', '2024-11-13 13:00:00', true, 1),
    ('Задача 7', '2024-11-13 14:00:00', false, 1);
