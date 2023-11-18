CREATE TABLE cities
(
    id   SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(40)        NOT NULL
);

CREATE TABLE users
(
    user_id    SERIAL PRIMARY KEY NOT NULL,
    first_name VARCHAR(100)       NOT NULL,
    last_name  VARCHAR(100)       NOT NULL,
    age        INT                NOT NULL,
    gender     VARCHAR(10)        NOT NULL,
    city_id    INT                NOT NULL,
    FOREIGN KEY (city_id) REFERENCES cities (id)
);

CREATE TABLE hobbies
(
    id          SERIAL      NOT NULL,
    user_id     INT         NOT NULL,
    name        VARCHAR(40) NOT NULL,
    description VARCHAR(255),
    PRIMARY KEY (id, user_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE credentials
(
    id            SERIAL       NOT NULL,
    user_id       INT          NOT NULL,
    login         VARCHAR(100) NOT NULL,
    pass          VARCHAR(100) NOT NULL,
    token         VARCHAR(50),
    token_expired DATE,
    enabled       BOOLEAN      NOT NULL,
    PRIMARY KEY (id, user_id, login),
    FOREIGN KEY (user_id) REFERENCES users (user_id)

);