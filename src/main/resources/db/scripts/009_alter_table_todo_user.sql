ALTER TABLE todo_user ADD COLUMN user_zone text;
UPDATE todo_user SET user_zone = 'UTC';