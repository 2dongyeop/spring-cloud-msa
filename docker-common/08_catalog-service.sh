#!/bin/bash

docker run -d --network ecommerce-network \
  --name catalog-service \
  -e "spring.datasource.url=jdbc:mysql://mysql-container:3306/mydb?useSSL=false&serverTimezone=Asia/Seoul&autoReconnect=true&useUnicode=yes&characterEncoding=UTF-8&allowPublicKeyRetrieval=true" \
  -e "kafka.server.ip=172.18.0.101:9092" \
  -e "eureka.client.service-url.defaultZone=http://discovery-service:8761/eureka/" \
  -e "logging.file=/api-logs/catalogs-ws.log" \
  leedongyeop/catalog-service:1.0.0