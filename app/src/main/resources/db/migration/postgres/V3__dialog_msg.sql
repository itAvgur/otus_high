CREATE TABLE messages
(
    id           SERIAL PRIMARY KEY NOT NULL,
    user_from_id INT                NOT NULL,
    user_to_id   INT                NOT NULL,
    text         VARCHAR            NOT NULL,
    created      TIMESTAMP          NOT NULL,
    FOREIGN KEY (user_from_id) REFERENCES users (user_id),
    FOREIGN KEY (user_to_id) REFERENCES users (user_id)
);