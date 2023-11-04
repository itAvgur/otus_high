#!/bin/sh
   docker exec -it postgres_otus psql otus_high -c "COPY cities FROM '/data/cities.csv' DELIMITER ',' CSV HEADER;" &&
   docker exec -it postgres_otus psql otus_high -c "ALTER SEQUENCE cities_id_seq restart with 130;" &&
   docker exec -it postgres_otus psql otus_high -c "COPY users FROM '/data/users.csv' DELIMITER ',' CSV HEADER;" &&
   docker exec -it postgres_otus psql otus_high -c "ALTER SEQUENCE users_id_seq restart with 1000001;" &&
   docker exec -it postgres_otus psql otus_high -c "COPY friends FROM '/data/friends.csv' DELIMITER ',' CSV HEADER;" &&
   docker exec -it postgres_otus psql otus_high -c "ALTER SEQUENCE friends_id_seq restart with 2000;" &&
   docker exec -it postgres_otus psql otus_high -c "COPY posts FROM '/data/posts.csv' DELIMITER ',' CSV HEADER;" &&
   docker exec -it postgres_otus psql otus_high -c "ALTER SEQUENCE posts_id_seq restart with 2000;" &&
   docker exec -it postgres_otus psql otus_high -c "COPY credentials FROM '/data/cred.csv' DELIMITER ',' CSV HEADER;" &&
   docker exec -it postgres_otus psql otus_high -c "ALTER SEQUENCE credentials_id_seq restart with 500;"