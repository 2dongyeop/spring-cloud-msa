#!/bin/bash

DIRECTORY=/Users/2dongyeop/Developments/kafka-demo/kafka_2.13-3.8.0

cd $DIRECTORY
./bin/zookeeper-server-start.sh ./config/zookeeper.properties