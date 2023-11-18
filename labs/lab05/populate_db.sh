#!/bin/sh
   docker exec -it otus-highload_master psql otus_high -c "COPY app.cities FROM '/data/cities.csv' DELIMITER ',' CSV HEADER;" &&
   docker exec -it otus-highload_master psql otus_high -c "ALTER SEQUENCE app.cities_id_seq restart with 130;" &&
   docker exec -it otus-highload_master psql otus_high -c "COPY app.users FROM '/data/users.csv' DELIMITER ',' CSV HEADER;" &&
   docker exec -it otus-highload_master psql otus_high -c "ALTER SEQUENCE app.users_user_id_seq restart with 1000001;" &&
   docker exec -it otus-highload_master psql otus_high -c "COPY app.friends FROM '/data/friends.csv' DELIMITER ',' CSV HEADER;" &&
   docker exec -it otus-highload_master psql otus_high -c "ALTER SEQUENCE app.friends_id_seq restart with 2000;" &&
   docker exec -it otus-highload_master psql otus_high -c "COPY app.posts FROM '/data/posts.csv' DELIMITER ',' CSV HEADER;" &&
   docker exec -it otus-highload_master psql otus_high -c "ALTER SEQUENCE app.posts_id_seq restart with 2000;" &&
   docker exec -it otus-highload_master psql otus_high -c "COPY app.credentials FROM '/data/cred.csv' DELIMITER ',' CSV HEADER;" &&
   docker exec -it otus-highload_master psql otus_high -c "ALTER SEQUENCE app.credentials_id_seq restart with 500;"