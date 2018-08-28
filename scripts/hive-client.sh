#!/usr/bin/env bash
java -Dconfig=../src/test/resources/test.properties -jar ../target/hive-client-*-dist.jar -e "$1"
