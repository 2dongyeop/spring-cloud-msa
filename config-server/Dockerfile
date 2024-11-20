FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp

# Config Server의 경우는 jks 키파일도 반드시 복사해줘야함.
COPY keystore/2dongyeop.jks 2dongyeop.jks
COPY target/config-service-1.0.0.jar ConfigServer.jar

ENTRYPOINT ["java", "-jar", "ConfigServer.jar"]
