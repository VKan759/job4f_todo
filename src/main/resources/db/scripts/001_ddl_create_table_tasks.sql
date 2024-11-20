CREATE TABLE tasks (
   id SERIAL PRIMARY KEY,
   title TEXT,
   description text,
   created TIMESTAMP,
   done BOOLEAN
);