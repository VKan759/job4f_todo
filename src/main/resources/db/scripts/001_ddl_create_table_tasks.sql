CREATE TABLE tasks (
   id SERIAL PRIMARY KEY,
   description TEXT,
   full_description text,
   created TIMESTAMP,
   done BOOLEAN
);