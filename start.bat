@echo off
docker run -d -p 5672:5672 -p 15672:15672 --name my-rabbit rabbitmq:3-management
start cmd /k "cd %1\eureka-server && mvnw spring-boot:run -e"
start cmd /k "cd %1\api-gateway && mvnw spring-boot:run -e"
start cmd /k "cd %1\user && mvnw spring-boot:run -e"
start cmd /k "cd %1\transaction && mvnw spring-boot:run -e"
start cmd /k "cd %1\account && mvnw spring-boot:run -e"

