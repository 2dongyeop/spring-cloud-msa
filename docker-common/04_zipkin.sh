#!/bin/bash

docker run -d -p 9411:9411 --network ecommerce-network \
  --name zipkin openzipkin/zipkin
