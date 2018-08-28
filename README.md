Hive Client For Integration Tests
=======

This small client helps you creating integration tests with Dockerized Hive images. 

It uses only uses Hive JDBC driver to run given queries but once you run the query(or queries) it waits until getting a proper connection. This make it useful to work with docker containers. It will fail if container doesn't show up before the timeout. 

Both inline queries and queries from file are supported.   

Example usage:
```
docker stop cloudera
docker rm cloudera
docker run --name cloudera --hostname=quickstart.cloudera --privileged=true --publish-all=true -t -i -p 10000:10000 -d cloudera/quickstart /usr/bin/docker-quickstart
./hive-client.sh -e "CREATE DATABASE test; CREATE TABLE test.test (test STRING); INSERT INTO test.test SELECT "test";
```

It requires a properties file for connection and timeout details:
```
# jdbc configuration
jdbc.driver=org.apache.hive.jdbc.HiveDriver
jdbc.url=jdbc:hive2://localdocker:10000/test
jdbc.user=
jdbc.pass=
# total timeout for running the query
jdbc.timeout=300
# amount of time before re-running the query
jdbc.wait=10
```
