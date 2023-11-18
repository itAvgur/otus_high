# caching

1) *run docker-compose* (for arm, for amd64 need use the other image ygmelnikov/otus_hl_amd:0.0.5)

docker-compose up -d

Populate DB: \
unzip ./populate_lab04.zip -d ~/pg/data \
./populate_db.sh

warm cache after population \
curl --location 'http://localhost:8080/manage/cache/warm' --header 'Content-Type: application/json' --data '{"cacheName": "post-feed"}'

2) *Postman (newman)*

   newman run cache.postman.json

3) redis cli \
   docker run --network lab04_otus --rm -it redis:alpine redis-cli -h redis_otus
