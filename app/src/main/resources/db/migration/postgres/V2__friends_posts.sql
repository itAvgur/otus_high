CREATE TABLE friends
(
    id        SERIAL NOT NULL,
    user_id   INT    NOT NULL,
    friend_id INT    NOT NULL,
    PRIMARY KEY (id, user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (friend_id) REFERENCES users (user_id)
);

CREATE TABLE posts
(
    id      SERIAL      NOT NULL,
    text    VARCHAR     NOT NULL,
    user_id INT         NOT NULL,
    status  VARCHAR(20) NOT NULL,
    created TIMESTAMP   NOT NULL,
    updated TIMESTAMP   NOT NULL,
    PRIMARY KEY (id, user_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)

);