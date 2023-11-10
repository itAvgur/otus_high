# otus_high

1) *run DB in docker*

   docker network create otus

   docker run --name otus_pg --network otus -p 5432:5432 -v ~/pg/main:/var/lib/postgresql/data -v ~/pg/data:/data -e POSTGRES_USER=root -e POSTGRES_PASSWORD=root -e POSTGRES_DB=otus_high -d postgres

2) *run application in Docker*

   **ARM:**\
   docker run -d -p 8080:8080 --network otus -e DB_TYPE=postgres ygmelnikov/otus_hl_arm:0.0.6

   **AMD64:**\
   docker run -d -p 8080:8080 --network otus -e DB_TYPE=postgres ygmelnikov/otus_hl_amd:0.0.6

3) *Postman (newman)*

   newman run {collection}

4) *Populate DB*
   untar sql scripts to ~/pg/data

   run SQL script:
   docker exec -it otus_pg psql otus_high -c "COPY cities FROM '/data/cities.csv' DELIMITER ',' CSV HEADER;" &&
   docker exec -it otus_pg psql otus_high -c "ALTER SEQUENCE cities_id_seq restart with 130;" &&
   docker exec -it otus_pg psql otus_high -c "COPY users FROM '/data/users.csv' DELIMITER ',' CSV HEADER;" &&
   docker exec -it otus_pg psql otus_high -c "ALTER SEQUENCE users_id_seq restart with 1000001;" &&
   docker exec -it otus_pg psql otus_high -c "COPY friends FROM '/data/friends.csv' DELIMITER ',' CSV HEADER;" &&
   docker exec -it otus_pg psql otus_high -c "ALTER SEQUENCE friends_id_seq restart with 930;" &&
   docker exec -it otus_pg psql otus_high -c "COPY posts FROM '/data/posts.csv' DELIMITER ',' CSV HEADER;" &&
   docker exec -it otus_pg psql otus_high -c "ALTER SEQUENCE posts_id_seq restart with 500;" &&
   docker exec -it otus_pg psql otus_high -c "COPY credentials FROM '/data/cred.csv' DELIMITER ',' CSV HEADER;" &&
   docker exec -it otus_pg psql otus_high -c "ALTER SEQUENCE credentials_id_seq restart with 500;"

5) redis\
   docker run -d -p 6379:6379 --name redis_otus --network otus redis

   CLI\
   docker run --network otus --rm -it redis:alpine redis-cli -h redis
