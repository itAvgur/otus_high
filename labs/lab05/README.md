# sharding

1) *run docker-compose*

   **ARM:**\
   docker-compose -f arm.yaml -p otus-highload up --scale worker=3 -d

   **AMD64:**\
   docker-compose -f amd64.yaml -p otus-highload up --scale worker=3 -d

2) *populate DB*

   ./populate_db.sh

3) *Postman (newman)*

   newman run sharding.postman.json

4) *Connect to citus master*

   docker exec -it otus-highload_master psql otus_high

5) *And reference tables (cities, hobbies) and distributive tables (users, credentials, friends, posts)*\
   SET schema 'app';
   SELECT create_reference_table('cities'); 
   SELECT create_distributed_table('users', 'user_id');
   SELECT create_distributed_table('hobbies', 'user_id', colocate_with=>'users');
   BEGIN;
   SET LOCAL citus.multi_shard_modify_mode TO 'sequential';
   SELECT create_distributed_table('posts', 'user_id', colocate_with=>'users');
   SELECT create_distributed_table('credentials', 'user_id', colocate_with=>'users');
   COMMIT;

6) *Add workers \
   docker-compose -f arm.yaml -p otus-highload up --scale worker=7 \
   
7) *Turn on logical application and restart* \
   alter system set wal_level = logical;
   SELECT run_command_on_workers('alter system set wal_level = logical');

8) *Rebalancing*\
   SELECT citus_rebalance_start();
   SELECT * FROM citus_rebalance_status();

   
   *) *commands*\
   SELECT master_get_active_worker_nodes();
   SELECT table_name,citus_table_type,nodename FROM citus_shards GROUP by nodename,table_name,citus_table_type order by table_name; \
   show wal_level;