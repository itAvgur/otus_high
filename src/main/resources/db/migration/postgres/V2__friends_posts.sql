DROP SCHEMA IF EXISTS otus_high CASCADE;

CREATE TABLE friends
(
    id        SERIAL PRIMARY KEY NOT NULL,
    user_id   INT                NOT NULL,
    friend_id INT                NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (friend_id) REFERENCES users (id),
    UNIQUE (user_id, friend_id)
);

CREATE TABLE posts
(
    id        SERIAL PRIMARY KEY NOT NULL,
    text      VARCHAR            NOT NULL,
    author_id INT                NOT NULL,
    status    VARCHAR(20)        NOT NULL,
    created   TIMESTAMP          NOT NULL,
    updated   TIMESTAMP          NOT NULL,
    FOREIGN KEY (author_id) REFERENCES users (id)

);