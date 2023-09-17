DROP SCHEMA IF EXISTS otus_high;
CREATE SCHEMA otus_high;

CREATE TABLE otus_high.cities
(
    id   INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name VARCHAR(40)                    NOT NULL
);

CREATE TABLE otus_high.users
(
    id         INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    first_name VARCHAR(100)                   NOT NULL,
    last_name  VARCHAR(100)                   NOT NULL,
    age        INT                            NOT NULL,
    gender     VARCHAR(10)                    NOT NULL,
    city_id    INT                            NOT NULL,
    FOREIGN KEY (city_id) REFERENCES otus_high.cities (id)
);

CREATE TABLE otus_high.hobbies
(
    id          INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    user_id     INT                            NOT NULL,
    name        VARCHAR(40)                    NOT NULL,
    description VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES otus_high.users (id)
);

CREATE TABLE otus_high.credentials
(
    id            INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    user_id       INT                            NOT NULL,
    login         VARCHAR(100)                   NOT NULL,
    pass          VARCHAR(100)                   NOT NULL,
    token         VARCHAR(50),
    token_expired DATE,
    enabled       BOOLEAN                        NOT NULL,
    FOREIGN KEY (user_id) REFERENCES otus_high.users (id)
);