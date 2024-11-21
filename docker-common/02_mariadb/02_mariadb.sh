#!/bin/bash

docker run -d --name mysql-container \
  --network ecommerce-network \
  --restart always \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=test1234 \
  -e TZ=Asia/Seoul \
  -v $(pwd)/mysql/data:/var/lib/mysql \
  mysql:8.0 \
  --character-set-server=utf8mb4

