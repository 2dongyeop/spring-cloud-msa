#!/bin/bash

docker run -d --name rabbitmq --network ecommerce-network \
 -p 5672:5672 -p 15672:15672 -p 4369:4369 -p 5671:5671 -p 15671:15671 \
 -e RABBITMQ_DEFAULT_USER=guest -e RABBITMQ_DEFAULT_PASS=guest \
 rabbitmq:management
