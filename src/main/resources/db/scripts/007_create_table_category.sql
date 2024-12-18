CREATE TABLE categories (
   id SERIAL PRIMARY KEY,
   name TEXT UNIQUE NOT NULL,
   position int
);

INSERT INTO categories (name) VALUES ('work');
INSERT INTO categories (name) VALUES ('personal');