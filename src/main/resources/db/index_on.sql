CREATE INDEX users_search_idx ON users (first_name, last_name);

CREATE INDEX cities_idx ON cities (name);

CREATE INDEX friend_user_id_idx ON friends (user_id, friend_id);

CREATE UNIQUE INDEX posts_author_id_idx ON posts (author_id)