CREATE TABLE participates (
   id SERIAL PRIMARY KEY,
   category_id int not null references categories(id),
   task_id int not null references tasks(id),
   UNIQUE (category_id, task_id)
);

insert INTO participates (category_id, task_id) VALUES (1, 1), (2, 1);