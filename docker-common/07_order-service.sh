#!/bin/bash

docker run -d --network ecommerce-network \
  --name order-service \
  -e "spring.cloud.config.uri=http://config-service:8888" \
  -e "spring.datasource.url=jdbc:mysql://mysql-container:3306/mydb?useSSL=false&serverTimezone=Asia/Seoul&autoReconnect=true&useUnicode=yes&characterEncoding=UTF-8&allowPublicKeyRetrieval=true" \
  -e "spring.rabbitmq.host=rabbitmq" \
  -e "kafka.server.ip=172.18.0.101:9092" \
  -e "spring.zipkin.base-url=http://zipkin:9411" \
  -e "management.zipkin.tracing.endpoint=http://zipkin:9411/api/v2/spans" \
  -e "eureka.client.service-url.defaultZone=http://discovery-service:8761/eureka/" \
  -e "logging.file=/api-logs/orders-ws.log" \
  leedongyeop/order-service:1.0.0