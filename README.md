# otus_high

1) *run Mysql in docker*

   docker network inspect otus   \
   docker run -p 3306:3306 --name otus_mysql --network otus -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=otus_high -d mysql:8.0.34

2) *run application in Docker*

!!!IMPORTANT!!!
There's a some bug, we need to run the application twice.
There's a some issue with FlyWay, it can't create flyway table while the first run.

   **ARM:**\
   docker run -d -p 8080:8080 --network otus -e OTUS_DB_TYPE=mysql ygmelnikov/otus_hl_arm

   **AMD64:**\
   docker run -d -p 8080:8080 --network otus -e OTUS_DB_TYPE=mysql ygmelnikov/otus_hl_amd64

3) *Postman (newman)*

   newman run high-available.postman_collection.json
