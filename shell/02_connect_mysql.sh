#!/bin/bash

echo ">> mysql -u root -ptest1234"

echo ">> connect mysql-container bash.."
docker exec -it mysql-container /bin/bash
