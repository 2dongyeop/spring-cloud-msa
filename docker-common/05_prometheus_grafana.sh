#!/bin/bash

docker run -d -p 9090:9090 \
 --network ecommerce-network \
 --name prometheus \
 -v /Users/2dongyeop/Developments/spring-cloud-msa/docker/prometheus/config/prometheus.yml:/etc/prometheus/prometheus.yml \
 prom/prometheus

# Grafana
docker run -d -p 3000:3000 \
 --network ecommerce-network \
 --name grafana \
 grafana/grafana