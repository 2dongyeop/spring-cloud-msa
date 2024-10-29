#!/bin/bash

DIRECTORY=/Users/2dongyeop/Developments/kafka-demo/confluent-7.7.1

cd $DIRECTORY
./bin/connect-distributed ./etc/kafka/connect-distributed.properties

