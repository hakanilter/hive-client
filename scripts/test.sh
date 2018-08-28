#!/usr/bin/env bash
docker stop cloudera
docker rm cloudera
docker run --name cloudera --hostname=quickstart.cloudera --privileged=true --publish-all=true -t -i -p 10000:10000 -d cloudera/quickstart /usr/bin/docker-quickstart
./hive-client.sh "CREATE DATABASE test; CREATE TABLE test.test (test STRING); INSERT INTO test.test SELECT 'test'; SELECT * FROM test"
docker stop cloudera
docker rm cloudera

