# otus_high

1) *run DB in docker*

   docker network create otus

   docker run --name otus_pg --network otus -p 5432:5432 -v ~/data:/data -e POSTGRES_USER=root -e POSTGRES_PASSWORD=root -e POSTGRES_DB=otus_high -d postgres

   2) *run application in Docker*

   **ARM:**\
   docker run -d -p 8080:8080 --network otus -e OTUS_DB_TYPE=postgres ygmelnikov/otus_hl_arm:0.0.3

   **AMD64:**\
   docker run -d -p 8080:8080 --network otus -e OTUS_DB_TYPE=postgres ygmelnikov/otus_hl_amd64:0.0.2

3) *Postman (newman)*

   newman run high-available.postman_collection.json

4) *Populate DB*
   copy (populate.zip) (cities.csv, users.csv) to ~/data
    
   run SQL script: 

   docker exec -it otus_pg psql otus_high -c "COPY cities FROM '/data/cities.csv' DELIMITER ',' CSV HEADER;" \
   docker exec -it otus_pg psql otus_high -c "COPY users FROM '/data/users.csv' DELIMITER ',' CSV HEADER;" \
   docker exec -it otus_pg psql otus_high -c "ALTER SEQUENCE cities_id_seq restart with 130;" \
   docker exec -it otus_pg psql otus_high -c "ALTER SEQUENCE users_id_seq restart with 1000001;"