## 목차

1. [Spring Cloud Netflix Eureka](#1-spring-cloud-netflix-eureka)
    - [Eureka Server 소개](#11-eureka-server-소개)
    - [Eureka Server 이용](#12-eureka-server-이용)
    - [Eureka Client 소개](#13-eureka-client-소개)
    - [Eureka Client 이용](#14-eureka-client-이용)
2. [API Gateway(Netflix Zuul, Spring Cloud Gateway)](#2-api-gateway-netflix-zuul-spring-cloud-gateway)
    - [API Gateway 소개](#21-api-gateway-소개)
    - [Spring Cloud Zuul](#22-spring-cloud-zuul)
    - [Spring Cloud Gateway](#23-spring-cloud-gateway)
    - [Spring Cloud Gateway Filter](#24-spring-cloud-gateway--filter-적용하기)
    - [Spring Cloud Gateway LoadBalancer](#25-spring-cloud-gateway--loadbalancer)
    - [Spring Cloud Gateway Predicates Filters](#26-spring-cloud-gateway--predicates--filters)
3. [Spring Cloud Config Server](#3-spring-cloud-config-server)
    - [Spring Cloud Config Server 소개](#31-spring-cloud-config-server-소개)
    - [Spring Cloud Config Server 장단점](#32-spring-cloud-config-server-장단점)
    - [Spring Cloud Config Server Config 적용](#33-spring-cloud-config-적용-방법)
    - [Spring Cloud Config Encrypt/Decrypt](#34-spring-cloud-config-encryptdecrypt)
    - [Config Server의 변경사항을 종료없이 Micro Service에 적용하기](#35-config-server의-변경-값을-micro-service에-적용하기)
4. [Spring Cloud Bus](#4-spring-cloud-bus)
    - [AMQP 설명](#41-amqp-설명)
    - [Spring Cloud Bus 동작](#42-spring-cloud-bus-동작-방식)
    - [Spring Cloud Bus 적용](#43-spring-cloud-bus-적용하기)
5. [설정 정보의 암호화 처리](#5-설정-정보의-암호화-처리-)
    - [암복호화 기본 개념](#51-암호화-기본-개념)
    - [설정파일 암복호화 활성화](#52-설정파일-암복호화-활성화)
6. [MicroService 간 통신](#6-microservice-간-통신)
    - [RestTemplate](#61-resttemplate)
    - [OpenFeign](#62-openfeign)
7. [데이터 동기화를 위한 Apache Kafka 활용](#7-데이터-동기화를-위한-apache-kafka-활용-1)
    - [Apache Kafka 개요](#7-1-apache-kafka-개요)
    - [Apache Kafka 서버 기동 및 튜토리얼](#7-2-apache-kafka-서버-기동-및-튜토리얼)
    - [Kafka Connect 개요 및 기동](#7-3-kafka-connect)
8. [장애 처리와 Microservice 분산 추적](#8-장애-처리와-microservice-분산-추적)
    - [Circuitbreaker & Resilience4j](#8-1-circuitbreaker--resilience4j)
    - [Zipkin을 이용한 Microservice 분산 추적 구현](#8-2-zipkin을-이용한-microservice-분산-추적)
9. [Microservice 모니터링](#9-마이크로서비스-모니터링)
    - [Micrometer 개요 및 구성](#9-1-micrometer-개요-및-구현)
    - [Prometheus 및 Grafana 를 이용한 모니터링 대시보드 구성](#9-2-prometheus-grafana를-이용한-모니터링-dashboad-구성)
10. [배포를 위한 컨테이너 가상화](#10-배포를-위한-컨테이너-가상화)
    - [Virtualization](#10-1-virtualization)
    - [Docker](#10-2-docker)
11. [애플리케이션 배포 구성](#11-애플리케이션-배포-구성)
    - [Docker Network 구성](#11-1-docker-network-구성)
    - [각 서비스들 가상화](#11-2-각-서비스들-가상화)
12. [Microservice Patterns](#12-microservice-patterns)
    - [Event Sourcing](#12-1-event-sourcing)
    - [CQRS : Command and Query Responsibility Segregation](#12-2-cqrs-command-and-query-responsibility-segregation)
    - [Saga Pattern](#12-3-saga-pattern)
13. [Spring Boot 3.2 + Spring Cloud 2023](#13-spring-boot-32--spring-cloud-2023)
    - [Eureka Service 이중화](#13-1-eureka-service-이중화)
14. [Kubernetes 배포](#14-kubernetes-배포)
    - [배포 목표 형태](#141-배포-목표-형태)
    - [k8s ConfigMap 동작 개념](#142-k8s-configmap-동작-개념)
    - [k8s Kafka 환경 구성](#143-k8s-kafka-환경-구성)
    - [k8s 명령어](#144-k8s-명령어)
    - [k8s 인프라 구성](#145-k8s-인프라-구성)

<br/>

# 1. Spring Cloud Netflix Eureka

Spring Cloud Eureka는 서비스 레지스트리와 서비스 디스커버리를 지원하는 라이브러리로써, 마이크로 서비스들의 정보를 저장하며, Server와 Client로 나뉜다.

<br/>

> Service Registry

마이크로서비스 / 관리, 운영을 위한 기반 서비스의 주소와 유동적인 IP를 매핑하여 저장하는 패턴

<br/>

> Service Discovery

클라이언트가 여러 개의 마이크로서비스를 호출하기 위해 최적 경로를 찾아주는 라우팅 기능과 적절한 부하 분산을 위한 로드 밸런싱 기능을 제공하는 패턴

<br/>

## 1.1 Eureka Server 소개

- REST API 기반으로 다양한 언어에서 사용 가능
- 레지스트리의 모든 정보는 모든 Eureka Client에서 복제되어 있으며 가용 중인 모든 서비스들의 목록을 확인할 수 있고 30초마다 목록이 갱신됨
- Eureka Client들에게 자신이 가지고 있는 Eureka Client들의 정보를 공유
- 가용 상태의 서비스 목록 확인 시 서비스의 이름을 기준으로 탐색, 로드 밸런싱을 위해 내부적으로 Ribbon (클라이언트 측의 로드 밸런서) 을 사용

<br/>

## 1.2 Eureka Server 이용

### 의존성 추가

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
</dependencies>
```

<br/>

### 유레카 서버 활성화

: `@EnableEurekaServer`을 이용해 유레카 서버를 활성화

```java

@EnableEurekaServer
@SpringBootApplication
public class DiscoveryserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryserviceApplication.class, args);
    }

}

```

<br/>

### 유레카 서버 GUI 활용

: 브라우저에서 `{serverUrl}:{serverPort}` 접속시, 등록된 마이크로 서비스들을 확인 가능

![img](image/eureka-1.png)


<br/>

## 1.3 Eureka Client 소개

- 서비스 시작 시 Eureka Server에 자신의 정보를 등록
- 등록된 후 30초마다 레지스트리에 핑을 전송하여 자신의 가용 상태를 알림
- 레지스트리로부터 다른 Eureka Client의 서비스 정보를 확인할 수 있음

<br/>

## 1.4 Eureka Client 이용

### 의존성 추가

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
</dependencies>
```

<br/>

### 유레카 클라이언트 활성화
: `@EnableDiscoveryClient`를 이용해 유레카 클라이언트 활성화

```java

@EnableDiscoveryClient
@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}

```

<br/>

### 유레카에 마이크로 서비스 등록

```yaml
spring:
  application:
    name: user-service         # 마이크로 서비스 이름

eureka:
  client:
    register-with-eureka: true # 유레카에 등록함을 명시
    fetch-registry: true       # 유레카 서버로부터 인스턴스들의 정보를 주기적으로 갱신된 정보를 가져옴
    service-url:
      defaultZone: http://127.0.0.1:8761/eureka # 유레카 서버 주소
```

<br/>

# 2. API Gateway (Netflix Zuul, Spring Cloud Gateway)

## 2.1 API Gateway 소개

![img](image/apigateway-1.png)

> API Gateway 역할

- 인증 및 권한 부여
- 서비스 검색 통합
- 응답 캐싱
- 정책, 회로 차단기 및 QoS 다시 시도
- 속도 제한
- 부하 분산
- 로깅, 추적, 상관관계
- 헤더, 쿼리 문자열 및 청구 변환
- IP 허용 목록에 추가

<br/>

## 2.2 Spring Cloud Zuul

Spring Boot 2.4에서 Maintenance 상태 (현재는 Deprecated)

<br/>

### 의존성 추가

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
    </dependency>
</dependencies>
```

<br/>

### Netflix Zuul 활성화
: `@EnableZuulProxy` 를 이용해 활성화

```java

@EnableZuulProxy
@SpringBootApplication
public class ZuulServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulServiceApplication.class, args);
    }

}

```

<br/>

### 마이크로 서비스 라우팅 설정

```yaml
spring:
  application:
    name: my-zuul-service

zuul:
  routes:
    first-service:
      path: /first-service/**     # 이 주소로 요청이 들어오면
      url: http://127.0.0.1:8081  # 이 주소로 요청을 보낸다.
    second-service:
      path: /second-service/**
      url: http://127.0.0.1:8082
```

<br/>

## 2.3 Spring Cloud Gateway

### 의존성 추가

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>
</dependencies>
```

<br/>

### 마이크로 서비스 라우팅 설정

```yaml
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
    service-url:
      defaultZone: http://localhost:8761/eureka

spring:
  application:
    name: apigateway-service
  cloud:
    gateway:
      routes:
        - id: first-service            # 마이크로 서비스 이름
          uri: http://localhost:8081/  # 마이크로 서비스 주소 (요청을 라우팅할 목적지)
          predicates:
            - Path=/first-service/**   # 라우팅 조건 (경로가 해당될 경우)
        - id: second_service
          uri: http://localhost:8082/
          predicates:
            - Path=/second-service/**
```

<br/>

## 2.4 Spring Cloud Gateway : Filter 적용하기

### 원하는 동작을 하는 필터 코드 작성

```java

@Slf4j
@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(LoggingFilter.Config config) {

        return new OrderedGatewayFilter((exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Logging Filter baseMessage: {}", config.getBaseMessage());

            if (config.isPreLogger()) {
                log.info("Logging PRE Filter: request id -> [{}]", request.getId());
            }

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if (config.isPostLogger()) {
                    log.info("Logging POST Filter: response code -> [{}]", response.getStatusCode());
                }
            }));
        }, Ordered.HIGHEST_PRECEDENCE);
    }

    @Data
    public static class Config {

        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
```

<br/>

### 설정 파일에서 필터 적용

```yaml
spring:
  application:
    name: apigateway-service
  cloud:
    gateway:
      default-filters:
        - name: GlobalFilter          # 전역 필터 적용시
          args: # GlobalFilter에서 사용되는 Config의 속성 바인딩
            baseMessage: Spring Cloud Gateway Global Filter
            preLogger: true
            postLogger: true
      routes:
        - id: first-service
          uri: http://localhost:8081/
          filters: # 특정 마이크로 서비스에만 필터 적용시
            - name: CustomFilter
            - name: LoggingFilter
              args: # LoggingFilter에서 사용되는 Config의 속성 바인딩
                baseMessage: Hi, Logging Filter
                preLogger: true
                postLogger: true
```

<br/>

## 2.5 Spring Cloud Gateway : LoadBalancer

Spring Cloud Gateway에는 로드밸런싱 기능이 기본 내장되어있다. (Spring Cloud LoadBalancer 와는 무관)

각 서비스를 주소와 포트번호로 식별하지 않고, 서비스 이름으로만 식별하는 방식으로 **요청을 라운드 로빈 방식으로 분산**시킨다.


<br/>

API Gateway의 환경 설정에서 마이크로 서비스들의 라우팅 정보를 아래와 같이 수정한다.

- AS-IS : **서버 주소와 포트를 명시**
    ```yaml
    spring:
    cloud:
        gateway:
        routes:
            - id: first-service
              uri: http://localhost:8081/   # {서버주소}:{서버포트}
    ```

- TO-BE : **서버의 애플리케이션 이름으로만 명시**
    ```yaml
    spring:
    cloud:
        gateway:
        routes:
            - id: first-service
              uri: lb://MY-FIRST-SERVICE    # {lb=로드밸런싱}:#{서비스이름}
    ```

<br/>

## 2.6 Spring Cloud Gateway : Predicates & Filters

아래에서는 Gateway에서 특정 조건이 만족할 경우, 요청 경로를 수정하는 예제다.

- RemoveRequestHeader=Cookie:
    - 이 필터를 요청에서 Cookie 헤더를 제거
- RewritePath=/user-service/(?<segment>.*), /$\{segment}
    - 이 필터는 요청 경로를 재작성 (ex. `/user-service/login` → `/login`)

- 예시
  ```yaml
  spring:
    application:
      name: apigateway-service
    cloud:
      gateway:
        default-filters:
          - name: GlobalFilter
            args:
              baseMessage: Spring Cloud Gateway Global Filter
              preLogger: true
              postLogger: true
        routes:
          - id: user-service
            uri: lb://USER-SERVICE
            predicates:
              - Path=/user-service/login  # 로그인 요청이 들어올 경우
              - Method=POST
            filters:
              - RemoveRequestHeader=Cookie
              - RewritePath=/user-service/(?<segment>.*), /$\{segment}
  ```

<br/>

# 3. Spring Cloud Config Server

![img](image/config-1.png)

## 3.1 Spring Cloud Config Server 소개

- Config Server는 여러 서비스(MSA) 들의 설정 파일을 외부로 분리해, 하나의 중앙 설정 저장소 처럼 관리 할 수 있도록 해주며, **특정 설정 값이 변경시 각각의 서비스를 재기동 할 필요없이 적용이
  가능하다.**
- 기본적으로 설정 정보 저장을 위해 git을 사용하도록 되어있어서 손쉽게 외부 도구들로 접근 가능하고, 버전 관리도 가능하다. (Git이 아닌, 서버에서 파일로 로컬 보관도 가능하다.)

<br/>

## 3.2 Spring Cloud Config Server 장단점

### 장점

1. 여러 서버의 설정 파일을 중앙 서버에서 관리할 수 있다.
2. 서버를 재배포 하지 않고 설정 파일의 변경사항을 반영할 수 있다.

<br/>

### 단점

1. Git 서버 또는 설정 서버에 의한 장애가 전파될 수 있다.
2. 우선 순위에 의해 설정 정보가 덮어씌워질 수 있다.
    - 설정 파일의 우선순위는 크게는 아래와 같다.
        1. 프로젝트의 application.yaml
        2. 설정 저장소의 application.yaml
        3. 프로젝트의 application-{profile}.yaml
        4. 설정 저장소의 {application name}/{application name}-{profile}

<br/>

## 3.3 Spring Cloud Config 적용 방법

### 의존성 추가

```xml

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
```

<br/>

### Config 서버 활성화

```java

@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
```

<br/>

### Config 서버 설정

```yaml
# Native(Local File System) 사용시
server:
  port: 8888

spring:
  application:
    name: config-service

  profiles:
    active: native # if config server target Native File System (Not Git)

  cloud:
    config:
      server:
        native:
          # http://localhost:8888/ecommerce/native <- check source properties
          search-locations: file://${user.home}/{config-file-path}
          # Native File System Location (Not Git)
  ---------------

# Git 사용시
server:
  port: 8888

spring:
  application:
    name: config-service

  cloud:
    config:
      server:
        git:
          # localhost:8888/ecommerce/default(or dev...) <- check source properties
          # UseCase 1 : Local Git Repository  
          # uri: /Users/2dongyeop/Developments/spring-cloud-config-server

          # UseCase 2 : Remote Public Git Repository
          uri: https://github.com/2dongyeop/spring-cloud-config-server.git

          # UseCase 3 : Remote Private Git Repository (+ UseCase 2)
          #username: $USERNAME
          #password: $PASSWORD
```

<br/>

### Micro Service에서 Config Server 참조하기

Spring Cloud Config Server에 ecommerce.yml이 있다고 가정할 때, 아래와 같이 Config-Server 위치와 파일명을 지정할 수 있다.

```yaml
# bootstrap.yml
# = application.yml보다 먼저 읽히는 설정 파일
spring:
  cloud:
    config:
      uri: http://127.0.0.1:8888
      name: ecommerce # config server에 위치한 yaml 파일 이름
```

<br/>

## 3.4 Spring Cloud Config Encrypt/Decrypt

- Config Server는 속성을 암호화하기 위해 대칭 키와 비대칭 키를 모두
  지원한다. → [참고자료](https://docs.spring.io/spring-cloud-config/reference/server/encryption-and-decryption.html)

<br/>

### 대칭키로 속성 암호화

```yaml
encrypt:
  key: example
```

<br/>

### 비대칭 키로 속성 암호화

RSA나 키스토어를 참조하는 방식을 말하며, 아래에서는 keytool 명령어를 설명한다.
아래 명령어를 실행할 경우, keystore.jks 파일이 생성된다.

```shell
keytool -genkeypair -alias {key-name} -keyalg RSA \
-dname "CN=Web Server, OU=Unit, O=Organization, L=City, S=State, C=US" \
-keypass {password} -keystore keystore.jks -storepass {secret}
```

<br/>

키 스토어를 애플리케이션 루트에 둘 경우, 아래와 같이 키스토어를 사용하도록 구성할 수 있다.

```yaml
encrypt:
  key-store:
    alias: { key-name }
    location: classpath:/keystore.jks
    password: { password }
    secret: { secret }
```

<br/>

위처럼 키 스토어가 준비된 후에는 아래와 같이 속성을 암호화해보자.

```shell
$ curl {config-server-url}:{port}/encrypt -d "plaintext"
-> {cipher-text}
```

이후에는 위에서 암호화한 값들을 속성에 작성하면 된다.

<br/>

## 3.5 Config Server의 변경 값을 Micro Service에 적용하기

### 1. Micro Service를 재부팅하기

- **이 방식은 서비스를 재부팅함으로써 다운타임이 생긴다는 치명적인 단점이 존재.**

<br/>

### 2. Spring Actuator Refresh 기능 이용하기

- Spring Actuator의 Refresh 엔드포인트를 이용하면, 참조하는 환경 설정파일을 새로고침 한다.
- 따라서 API Server를 재시작하지 않고, 변경 사항을 적용할 수 있다.

1. Spring Actuator 의존성 추가하기

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

2. application.yml에 endpoint 추가하기

```yaml
management:
  endpoints:
    web:
      exposure:
        include: refresh
```

3. Refresh Endpoint 호출하기
   Actuator의 다른 Endpoint(Health, Info ...)와 달리 POST 방식으로 요청해야 함.

```shell
curl -X POST "{Server Url}:{port}/actuator/refresh"
```

**단, 이 방식은 마이크로 서비스가 수백개가 넘는 등의.. 수많은 설정 파일로 구성된다면 일일이 refresh를 시켜주기에는 무리가 있음.**

<br/>

### 3. Spring Cloud Bus 이용하기

- 분산 시스템의 노드(Micro Service에 해당)를 경량 메시지 브로커(ex. RabbitMQ)와 연결
- 상태 및 구성에 대한 변경 사항을 연결된 노드에게 전달하는 방식
- 자세한 내용은 [4. Spring Cloud Bus](#4-spring-cloud-bus)에서 확인

<br/>

# 4. Spring Cloud Bus

## 4.1 Spring Cloud Bus 동작 방식

![spring-cloud-bus-3.png](image%2Fspring-cloud-bus-3.png)

- Spring Cloud Bus 로 연결된 서비스들 중 어느 한 곳이라도 `/busrefresh` endpoint를 호출 시, 모든 서비스에 변경 사항이 반영
- 이때, 변경 정보는 AMQP 프로토콜로 전달하는 방식

<br/>

## 4.2 AMQP(Advanced Message Queuing Protocol)

- 메시지 지향 미들웨어를 위한 개방형 표준 응용 계층 프로토콜
    - 큐잉, 라우팅(P2P, Pub-Sub), 신뢰성 및 보안을 강조
    - Erlang, RabbitMQ, Kafka, Apache Pulsar 등이 존재

<br/>

### RabbitMQ와 Kafka 비교

- RabbitMQ
    - 메시지 브로커
    - 초당 20+ 메시지를 소비자에게 전달
    - 메시지 전달 보장, 시스템 간 메시지 전달
    - 브로커, 소비자 중심
- Kafka
    - 분산형 스트리밍 플랫폼, 대용량의 데이터를 처리 가능한 메시징 시스템
    - 초당 100k+ 이상의 이벤트 처리
    - Pub/Sub, Topic에 메시지 전달
    - Ack를 기다리지 않고 전달 가능(성능이 빠름)
    - 생산자 중심
- 성능 비교

  ![img](image/rabbitmq-kafka-1.png)

<br/>

## 4.3 Spring Cloud Bus 적용하기

### RabbitMQ 실행

- 5672 포트 : AMQP 프로토콜로 데이터 전송시 사용
- 15672 포트 : RabbitMQ UI

```bash
$ docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

### Config Server

1. 의존성 추가

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.cloud</groupId>
<artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.cloud</groupId>
<artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>
```

2. RabbitMQ 설정 파일 작성 (busrefresh endpoint 필수)

```yaml
spring:
  ...

  rabbitmq:
    host: 127.0.0.1
    port: 5672 # 5672 is AMQP protocol port. 15672 is RabbitMQ UI port
    username: guest # default value
    password: guest # default value 

management:
  endpoints:
    web:
      exposure:
        include: health, busrefresh
```

### 기타 MicroService 및 API Gateway

1. 의존성 추가(액츄에이터가 이미 추가되어 있다는 가정)

```xml

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
```

2. RabbitMQ 설정 파일 작성

```yaml
spring:
  ...

  rabbitmq:
    host: 127.0.0.1
    port: 5672 # 5672 is AMQP protocol port. 15672 is RabbitMQ UI port
    username: guest # default value
    password: guest # default value

management:
  endpoints:
    web:
      exposure:
        include: refresh, health, beans, busrefresh
```

### Bus Refresh 동작 호출

RabbitMQ로 Bus refresh를 동작하도록 추가했다면, 어느 Micro Service를 호출해도 상관없다.

```bash
# 아래의 localhost:8000 은 API Gateway 주소입니다.
# API Gateway의 Bus Refresh 호출 시
curl -X POST http://localhost:8000/actuator/refresh

# User Service의 Bus Refresh 호출 시
curl -X POST http://localhost:8000/user-service/actuator/refresh
```

<br/>

# 5. 설정 정보의 암호화 처리

## 5.1 암호화 기본 개념

> Symmetric Encryption(Shared)

- 대칭 암호화 방식
- 암호화/복호화 할 때의 키가 동일

> Asymmetric Encryption(RSA Keypair)

- 암호화/복호화 할 때의 키가 다름
- Private/Public Key 방식
- 혹은 Using Java Keytool(JDK 내장 도구로, private/public 키를 생성)

> 동작 방식

- Spring Cloud Server에서는 `{cipher}` 꼴로 시작하는 암호화된 환경 설정 파일을 저장
- 이후, 이를 참조하는 각각의 Micro Service 들에게는 복호화된 설정 파일을 제공하는 방식
  ![enc:decryption.png](image%2Fenc%3Adecryption.png)

<br/>

## 5.2 설정파일 암복호화 활성화

### 1. jks 파일 생성하기

```bash
$ keytool -genkeypair -alias 2dongyeop \
  -keyalg RSA -dname "CN=Dongyeop Lee, OU=Development, O=songareeit.com, L=Seoul, C=KR" \
  -keypass "example1234" -keystore 2dongyeop.jks -storepass "example1234"
```

- `keytool` : Java의 keytool 유틸리티 실행 명령어
- `-genkeypair` : 공개키와 개인키를 생성하는 옵션
- `-alias` : 키의 별칭 지정
- `keyalg RSA` : RSA 알고리즘으로 키 쌍을 생성
- `-dname` : 인증서에 포함될 사용자 정보를 지정
    - `CN (Common Name), OU(Organizational Unit), O(Organization), L(Location), C(Country)`
- `-keypass` : 개인키 암호 지정
- `-keystore` : 키스토어 파일의 경로와 이름을 지정
- `-storepasss` : 키스토어 파일 자체의 암호를 지정
- `-validity` : 인증 기한 설정. 일(day) 단위이며, 기본 값은 90일.

### 2. 키파일 위치 지정

```yaml
# Config Server 설정파일
encrypt:
  # 단방향 암호화. keytool을 이용한 .jks 파일 필요 x
  # key: abcdefghijklmnopqrstuvwxyz0123456789
  key-store:
    location: file://${user.home}/{경로}/{키 이름}.jks
    password: example1234
    alias: 2dongyeop
```

### 3. 암복호화 결과 확인

```bash
$ curl localhost:8888/encrypt -s -d test

-> ASFAVIDSJVSDKDVS....
```

### 4. 암호화된 값을 설정파일에 적용

```yaml
# AS-IS
spring:
  datasource:
    url: jdbc:h2:tcp://localhost/~/testdb
    username: sa
    password:
    driver-class-name: org.h2.Driver
#############################################
# TO-BE
spring:
  datasource:
    url: jdbc:h2:tcp://localhost/~/testdb
    username: '{cipher}AQBd5MeVwOVNJ......'
    password:
    driver-class-name: org.h2.Driver
```

<br/>

# 6. MicroService 간 통신

## 6.1 RestTemplate

![resttemplate1.png](image/resttemplate1.png)

### 1. RestTemplate 소개

- 스프링 프레임워크에서 제공하는 RESTful API 통신을 위한 도구
    - 다양한 HTTP Method를 사용하며, 외부 서버와 동기식 방식으로 통신한다.
    - 동기식 으로 요청을 보내고 응답을 받을 때까지 블로킹된다. (응답을 기다린다.)
- Spring 공식 문서에서 RestTemplate을 확인하면 아래와 같이 내용이 있다.
    - 동기식 HTTP 접근에 사용하고 있을 경우 → Spring 6.1에 나온 RestClient 사용을 권장
    - 비동기 및 스트리밍 시나리오의 경우 → WebClient 사용을 권장

### 2. RestTemplate 빈 등록 방식

```java

@Configuration
public class RestTemplateConfig {

    @Value("${restTemplate.connectTimeOut}")
    private Long connectTimeOut;
    @Value("${restTemplate.readTimeOut}")
    private Long readTimeOut;

    @Bean
    @Qualifier("restTemplate")
    public RestTemplate restTemplate(final RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(connectTimeOut)) // 외부 API 서버에 연결 요청 시간
                .setReadTimeout(Duration.ofSeconds(readTimeOut))       // 외부 API 서버로부터 데이터를 읽어오는 시간
                .build();
    }
}
```

### 3. RestTemplate 단점

- 비즈니스 코드에 지저분한 설정들이 섞인다.
- Retry를 구현할 경우, 추가 의존성(spring-retry, spring-boot-starter-aop)을 필요로 한다.
- 제공되는 로그가 자세하지 않다.

<br/>

## 6.2 OpenFeign

### 1. OpenFeign 소개

- Netflix에 의해 처음 만들어진 Declarative(선언적) HTTP Client 도구로써, 외부 API 호출을 쉽게 할 수 있도록 도와준다.
- 여기서 선언적이란 애너테이션 사용을 의미한다.

### 2. OpenFeign 장점

- 인터페이스와 애너테이션 기반으로 작성할 코드가 줄어듬
- 익숙한 Spring MVC 애너테이션으로 개발이 가능
- 다른 Spring Cloud 기술들 (Eureka, Circuit Breaker, LoadBalancer) 과의 통합이 쉬움

### 3. OpenFeign 기능

- 타임아웃, 재시도(Retry) 지원
- 각각의 Feign Client별로 로그 레벨을 지정 가능
- Fallback 설정 가능
- Fallback : 실행을 실패(Exception)하는 경우에 대신 실행되게하는 프로세스
- Error Handling (Error Decoder) 지원
- RequestInterceptor 지원
- 위 기능들을 모두 구성 속성(application.yml)로 설정 가능

### 4. 사용 방법

1. 의존성 추가

```xml

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
    <version>{version}</version>
</dependency>
```

2. Fegin Client 활성화

```java

@EnableFeignClients // Feign Client 활성화
@SpringBootApplication
public class RequestServerApplication { ...
}
```

3. Feign Client 정의

```java

@FeignClient(value = "order-service", url = "${url.order-service.endpoint")
public interface OrderServiceClient {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse send(@RequestBody CommonRequest request);
}
```

4. Feign Client 호출

```java

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final OrderFeignClient orderFeignClient;

    /**
     * FeignClient로 외부 API를 호출하는 예시
     */
    public CommonResponse send(final CommonRequest request) {
        log.debug("request[{}]", request);
        return orderFeignClient.send(request);
    }
}

```

5. 기타 - 로그 설정 및 에러 처리

```java

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(final String methodKey, final Response response) {
        log.warn("statusCode[{}], methodKey[{}]", response.status(), methodKey);

        return switch (response.status()) {
            case 400 -> ...
            case 404 -> ...
            case 500 -> ...
            default -> ...
        };
    }
}
``` 

<br/>

# 7. 데이터 동기화를 위한 Apache Kafka 활용-1

## 7-1. Apache Kafka 개요

### 1. Apache Kafka 소개

- Apache Software Foundation의 Scalar 언어로 된 오픈소스 메시지 브로커
- 링크드인에서 개발 (2011년 오픈소스화). 이후 엔지니어들이 Kafka 개발에 집중하기 위해 Confluent 창립

### 2. Apache Kafka 특징

- Producer/Consumer 분리
- 메시지를 여러 Consumer에게 허용
- 높은 처리량을 위한 메시지 최적화
- Scale-out 가능, Eco-system

### 3. Apache Kafka 구성

- 실행된 Kafka 애플리케이션 서버
- 3대 이상의 Broker Cluster 구성
    - n대 중 1개는 Controller 역할
    - = 각 Broker에게 담당 파티션 할당 수행 & Broker 정상 동작 모니터링
- Zookeeper 연동
    - 메타데이터 (Broker ID, Controller ID 등) 저장

### 4. Apache Kafka 설치

- Docker 를 이용해서도 환경 구성은 가능. 강의에서는 직접 설치하는 방식
- [공식 홈페이지 설치 링크](https://kafka.apache.org/downloads)

## 7-2. Apache Kafka 서버 기동 및 튜토리얼

> 아래 모든 명령어를 실행시키는 위치는 Kafka를 다운로드한 위치(`$KAFKA_HOME`)입니다.

### 1. Zookeeper 및 Kafka 서버 기동

```shell
# Zookeeper 실행
$ ./bin/zookeeper-server-start.sh ./config/zookeeper.properties

# Kafka 실행
$ ./bin/kafka-server-start.sh ./config/server.properties
```

### 2. Topic 생성 및 목록 확인

```shell
# 토픽 생성
$ ./bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic quickstart-events --partitions 1

# 토픽 목록 확인
$ ./bin/kafka-topics.sh --bootstrap-server localhost:9092 --list

# 특정 토픽 상세 조회
$ ./bin/kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic quickstart-events
```

### 3. Producer 및 Consumer 실행

```shell
# Producer 실행
$ ./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic quickstart-events
> {전달할 메시지 입력}

# Consumer 실행
$ ./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic quickstart-events --from-beginning
{시작 직후 즉시 메시지 도착 & 이전에 해당 토픽으로 도착한 메시지가 있다면 즉시 노출.}
```

## 7-3. Kafka Connect

![kafka-connect-01.png](image/kafka-connect-01.png)

### 1. Kafka Connect 소개

- Data를 Import/Export 하는 기능 제공
- 코드를 작성하지 않고 Configuration으로 데이터를 이동
- Standalone mode & Distribution mode 지원
    - RESTful API 통해 지원
    - Stream 또는 Batch 형태로 데이터 전송 가능
    - 커스텀 Connector를 통한 다양한 Plugin 제공(File, S3, Hibe, Mysql, etc ...)

### 2. Kafka Connect 설치 및 설정

#### Kafka Connect 설치

```shell
$ curl -O https://packages.confluent.io/archive/7.7/confluent-community-7.7.1.tar.gz
$ tar xvf confluent-community-7.1.1.tar.gz
```

#### JDBC Connector 설치

- [JDBC Connector 설치 링크](https://www.confluent.io/hub/confluentinc/kafka-connect-jdbc?session_ref=https://www.google.com/&_ga=2.258331402.1052886943.1729079800-1664852375.1729079799&_gl=1*se5yq4*_gcl_au*MTQxNTg1NzI5OC4xNzI5MDc5Nzk5*_ga*MTY2NDg1MjM3NS4xNzI5MDc5Nzk5*_ga_D2D3EGKSGD*MTcyOTA3OTc5OS4xLjEuMTcyOTA4MTM5OS41OS4wLjA.)

#### JDBC Connector를 플러그인으로 추가

```shell
# confluent-7.1.1 설치 위치에서
$ code ./etc/kafka/connect-distributed.properties
>>>>>
 plugin.path=/Users/2dongyeop/Developments/kafka-demo/confluentinc-kafka-connect-jdbc-10.8.0/lib
<<<<< EOF
```

#### JDBC Connector에서 사용할 MySQL Driver 복사

```shell
# Order Service에 mariadb-java-client 의존성을 추가했다면 로컬 mvn repository에 jar이 존재.
cp /Users/2dongyeop/.m2/repository/org/mariadb/jdbc/mariadb-java-client/2.7.2/mariadb-java-client-2.7.2.jar \
   /Users/2dongyeop/Developments/kafka-demo/confluent-7.1.1/share/java/kafka
```

### 3. Kafka Source/Sink Connector 실행

#### Kafka Connect 실행

```shell
# Connect가 설치된 위치에서
$ ./bin/connect-distributed ./etc/kafka/connect-distributed.properties
```

#### Kafka Source Connect 추가

```shell
POST localhost:8083/connectors # Connector 주소
BODY
{
    "name": "my-source-connect",
    "config": {
        "connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector",
        "connection.url": "jdbc:mysql://localhost:3306/mydb?useSSL=false&serverTimezone=Asia/Seoul&autoReconnect=true&useUnicode=yes&characterEncoding=UTF-8&allowPublicKeyRetrieval=true",
        "connection.user": "root",
        "connection.password": "test1234",
        "mode": "incrementing",
        "incrementing.column.name": "id",
        "table.whitelist": "users",
        "topic.prefix": "my_topic_",
        "tasks.max": "1"
    }
}
```

#### Kafka Source Connect 조회

```shell
# 목록 조회
curl localhost:8083/connectors

# 상세 조회
curl localhost:8083/connectors/my-source-connect
```

#### Kafka Source Connect 동작 확인

1. Consumer 기동

```shell
./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic my-source-connect --from-beginning
```

2. MySQL 접속 후 데이터 삽입

```sql
insert into users(user_id, pwd, name)
values ('user1', 'test1111', 'username');
```

3. Consumer 로그 확인

```text
{"schema":{"type":"struct","fields":[{"type":"int32","optional":false,"field":"id"},{"type":"string","optional":true,"field":"user_id"},{"type":"string","optional":true,"field":"pwd"},{"type":"string","optional":true,"field":"name"},{"type":"int64","optional":true,"name":"org.apache.kafka.connect.data.Timestamp","version":1,"field":"created_at"}],"optional":false,"name":"users"},"payload":{"id":1,"user_id":"user1","pwd":"test1111","name":"username","created_at":1729631568000}}
```

#### Kafka Sink Connect 추가

- 등록과 동시에 DB에 `my_topic_users` 테이블이 생성되며,
- `user` 테이블에서 변경사항이 생길 경우 `my_topic_users` 테이블에 함께 적용됨.
    - → 이러한 점을 토대로 데이터를 옮기는 ETL 용도로 사용 가능.

```shell
POST localhost:8083/connectors # Connector 주소
BODY
{
    "name": "my-sink-connect",
    "config": {
        "connector.class": "io.confluent.connect.jdbc.JdbcSinkConnector",
        "connection.url": "jdbc:mysql://localhost:3306/mydb?useSSL=false&serverTimezone=Asia/Seoul&autoReconnect=true&useUnicode=yes&characterEncoding=UTF-8&allowPublicKeyRetrieval=true",
        "connection.user": "root",
        "connection.password": "test1234",
        "auto.create": "true",
        "auto.evolve": "true",
        "delete.enabled": "false",
        "tasks.max": "1",
        "topics": "my_topic_users"
    }
}
```

### 4. Kafka를 이용한 데이터 동기화 구성

- 사전 배경 : 아래와 같이 구성한 뒤, kafka & zookeeper 서버를 기동한 상태
- 동작 확인 : 주문을 생성할 경우, Catalog의 Stock이 감소하는지 확인

#### Kafka Consumer 구성

1. 의존성 추가
    ```xml
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
    ```

2. KafkaConsumerConfig 작성

  ```java

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${kafka.server.ip:localhost:9092}")
    private String kafkaServerIp;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        final HashMap<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerIp);
        // 다수의 Consumer 가 존재할 경우, 특정 그룹만이 소비하도록 할 수 있음.
        // 현재처럼 1개의 Consumer 만 존재할 경우에는 아무 소용x
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumerGroupId");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        final ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory());

        return kafkaListenerContainerFactory;
    }
}
  ```

3. KafkaConsumer 작성
    ```java
    @Slf4j
    @Service
    @RequiredArgsConstructor
    public class KafkaConsumer {
    
        private final CatalogRepository catalogRepository;
    
        @KafkaListener(topics = "example-catalog-topic")
        public void updateQty(final String kafkaMessage) {
            log.info("kafkaMessage[{}]", kafkaMessage);
    
            final ObjectMapper mapper = new ObjectMapper();
            HashMap<Object, Object> map = new HashMap<>();
    
            try {
                map = mapper.readValue(kafkaMessage, new TypeReference<>() {
                });
            } catch (JsonProcessingException jsonProcessingException) {
                log.error("", jsonProcessingException);
            }
    
            /* 비즈니스 로직.. */
            final CatalogEntity catalog = catalogRepository.findByProductId(map.get("productId").toString());
            log.info("catalog[{}]", catalog);
    
            if (catalog != null) {
                catalog.setStock(catalog.getStock() - (Integer) map.get("qty"));
                catalogRepository.save(catalog);
            }
        }
    }
    ```

#### Kafka Producer 구성

1. 의존성 추가
    ```xml
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
    ```

2. KafkaProducerConfig 작성
    ```java
    @EnableKafka
    @Configuration
    public class KafkaProducerConfig {
    
        @Value("${kafka.server.ip:localhost:9092}")
        private String kafkaServerIp;
    
        @Bean
        public ProducerFactory<String, String> producerFactory() {
            final HashMap<String, Object> properties = new HashMap<>();
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerIp);
            properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    
            return new DefaultKafkaProducerFactory<>(properties);
        }
    
        @Bean
        public KafkaTemplate<String, String> kafkaTemplate() {
            return new KafkaTemplate<>(producerFactory());
        }
    }
    ```


3. KafkaProducer 작성
    ```java
    @Slf4j
    @Service
    @RequiredArgsConstructor
    public class KafkaProducer {
    
        private final KafkaTemplate<String, String> kafkaTemplate;
    
        public OrderDto send(String topic, OrderDto orderDto) {
    
            final ObjectMapper mapper = new ObjectMapper();
            String json = "";
    
            try {
                json = mapper.writeValueAsString(orderDto);
            } catch (JsonProcessingException e) {
                log.error("", e);
            }
    
            kafkaTemplate.send(topic, json);
            log.info("[Kafka] to Order microService : {}", orderDto);
    
            return orderDto;
        }
    }
    ```

<br/>

# 8. 장애 처리와 Microservice 분산 추적

## 8-1. CircuitBreaker & Resilience4J

![circuitBreaker01.png](image/circuitBreaker01.png)

### 1. CircuitBreaker 소개

- 장애가 발생하는 서비스에 반복적인 호출을 하지 않도록 차단하는 역할
- 특정 서비스가 정상적으로 동작하지 않을 경우, 다른 기능으로 대체 수행
    - → 장애 회피

### 2. CircuitBreaker 적용하기

> AS-IS (Spring Boot 2.3 & Spring Cloud 2020 이전)

1. 의존성 추가

      ```xml
    <dependency>
        <groupId>org.springframework.cloyd</groupId>
        <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
    </dependency> 
      ```

2. 활성화 애너테이션 지정
    ```java
    @EnableCircuitBreaker
    public class Application {...}
    ```

3. 활성화 환경설정 지정
    ```yaml
    feign:
      hystrix:
        enabled: true
    ```

<br/>

> TO-BE (Spring Cloud 2020 이후)

- `netflix-hystrix` 가 지원이 종료되어, `Resilience4j` 를 사용
- 아래 내용 참고

<br/>

### 3. Resilience4j 소개

- Netflix Hystrix 로부터 영감받은 경량 라이브러리 ([출처](https://resilience4j.readme.io/))
- Fault Tolerance : 에러가 발생해도 정상 서비스 운영이 가능한 성격
- 내부 기능
    - resilience4j-circuitbreaker
    - resilience4j-ratelimiter
    - resilience4j-bulkhead
    - resilience4j-retry (sync and async)
    - resilience4j-timelimiter
    - resilience4j-cache (Result caching)

<br/>

### 4. Resilience4j 적용하기

> Default Configuration 이용 시

1. 의존성 추가
    ```xml
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
    </dependency>
    ```

2. 코드 작성
    ```java
    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;
    
    ...
    
    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
    List<ResponseOrder> ordersList = circuitBreaker.run(() -> orderServiceClient.getOrders(userId),
            throwable -> new ArrayList<>());
    ```

<br/> 

> Custom Configuration 이용 시

1. 설정 클래스 작성
   ```java
    @Configuration
    public class Resilience4jConfiguration {
        @Bean
        public Customizer<Resilience4JCircuitBreakerFactory> globalCustomCircuitBreakerFactory() {
            /*
             * failureRateThreshold() : CircuitBreaker를 활성화(open) 할 지를 결정하는 실패 비율. 기본 50
             * waitDurationInOpenState() : CircuitBreaker 활성화 상태를 유지하는 지속 시간. 기본값 60s
             * slidingWindowType() 
                - CircuitBreaker가 비활성화(closed)될 때 통화 결과를 기록하는 데에 사용되는 슬라이딩 창의 유형
                - 카운트 기반/시간 기반 
             * slidingWindowSize() 
                - CircuitBreaker가 비활성화(closed)될 때 호출 결과를 기록하는 데에 사용되는 슬라이딩 창의 크기 구성
                - 기본값 100
             */
            final CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                    .failureRateThreshold(50) 
                    .waitDurationInOpenState(Duration.ofMillis(60000))
                    .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                    .slidingWindowSize(100)
                    .build();
   
            /* 
             * TimeLimiter : n초 동안의 응답이 없을 경우, 에러로 간주
             * default : 1초 
             */
            final TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                    .timeoutDuration(Duration.ofSeconds(1))
                    .build();

            return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                    .timeLimiterConfig(timeLimiterConfig)
                    .circuitBreakerConfig(circuitBreakerConfig)
                    .build());
        }
    }
    ```

<br/>

## 8-2. Zipkin을 이용한 Microservice 분산 추적

### 1. [Zipkin](https://zipkin.io/) 소개

- Twitter에서 사용하는 분산 환경의 Timing 데이터 수집, 추적 오픈소스
- 분산환경에서의 시스템 병목 현상 파악
- Collector, Query Service(API), Database(Storage), Web UI로 구성

<br/>

> Span과 Trace
> - Span : 하나의 요청에서 사용되는 작업의 단위
> - Trace : 하나의 요청에 대한 같은 Trace ID 발급(트리 구조로 이루어진 Span 셋)

<br/> 

### 2. ~~Spring Cloud Sleuth~~ Micrometer-Tracing

- **Spring Boot 3.x 부터는 Sleuth가 Deprecated되어 Micrometer-tracing을 사용해야 함.**
- 스프링부트 애플리케이션을 Zipkin과 연동
- 요청 값에 따른 Trace ID, Span ID를 부여

<br/>

### 3. Zipkin 분산 추적 구성

1. Zipkin 기동

```shell
# Docker 이용시 (설치하여 사용하는 방식은 생략.)
$ docker run -d -p 9411:9411 openzipkin/zipkin
```

2. Zipkin 및 Micrometer Tracing 의존성 추가

```xml
<!-- Micrometer Tracing으로 MSA 분산 추적 구현 -->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing-bridge-brave</artifactId>
</dependency>

        <!-- 분산 추적 내용을 zipkin으로 모니터링 -->
<dependency>
<groupId>io.zipkin.reporter2</groupId>
<artifactId>zipkin-reporter-brave</artifactId>
</dependency>
``` 

3. 설정파일에 zipkin 주소 및 샘플링 설정 추가

```yaml
 management:
   tracing:
     sampling:
       probability: 1.0   # 로그 샘플링 비율 - 1.0일 경우에는 100% 로그를 샘플링, default : 0.1
     propagation: # 추적 정보 전파 방식 (wbc, b3, b3_multi)
       produce: b3_multi  # 추적 정보를 여러 개의 헤더로 나누어 전송
       consume: b3        # HTTP 헤더를 사용하여 추적 ID, 스팬 ID 등을 전달

   zipkin:
     tracing:
       endpoint: "http://localhost:9411/api/v2/spans"

 logging:
   pattern:
     level: "%5p [%X{traceId:-},%X{spanId:-}]"
```

4. Zipkin UI에서 요청 추적

- 우측 검색 창에 Trace ID 입력 시, 확인 가능
  ![zipkin.png](image/zipkin.png)

<br/>

# 9. 마이크로서비스 모니터링

## 9-1. Micrometer 개요 및 구현

### 1. Micrometer 개요

- 이전에는 Hystrix Dashboard / Turbin을 이용했지만, Spring 5 & Spring Boot 2부터 Deprecated 되어 [Micromter](https://micrometer.io)를
  사용해야 함.
- JVM 기반의 애플리케이션의 Metrics 제공
- Prometheus 등의 다양한 모니터링 시스템 제공

<br/>

> Timer란?
> - 짧은 지연시간, 이벤트의 사용 빈도를 측정
> - 시계열로 이벤트의 시간, 호출 빈도 등을 제공
> - `@Timed` 애너테이션 제공

<br/>

### 2. Micrometer 구현

1. 의존성 추가
    ```xml
    <!-- 사전에 actuator를 추가해놓았다는 가정.-->
    <!-- Micrometer 지표를 Prometheus로 보내는 라이브러리 -->
    <dependency>
        <groupId>io.micrometer</groupId>
        <artifactId>micrometer-registry-prometheus</artifactId>
    </dependency>
    ```

2. 설정파일에서 endpoints 추가
    ```yaml
    management:
      endpoints:
        web:
          exposure:
            include: health, httptrace, info, metrics, prometheus
    ```

3. (필수 아님) 원하는 지표가 있을 경우, `@Timed` 애너테이션 활용
    - 해당 애너테이션은 API 레벨에서 관리되며, API가 호출될 경우 집계됨.
    - 이후, 몇번 호출 되었는지 등을 집계할 수 있음.
    ```java
    @Timed(value = "users.status", longTask = true)
    @GetMapping("/health_check")
    public String status() {
        log.debug("health_check health_check");
    
        return "[User Service]\n" + String.format("It's Working in User Service on PORT %s",
                env.getProperty("local.server.port")) + "\ntoken.expiration_time =" + env.getProperty("token.expiration_time")
                + "\ntoken.secret = " + env.getProperty("token.secret");
    }
    ```

<br/>

## 9-2. Prometheus, Grafana를 이용한 모니터링 Dashboad 구성

<br/>

### 1. Prometheus 소개

- Metrics 를 수집하고 모니터링 및 알람에 사용되는 오픈소스 애플리케이션
- 2016년부터 CNCF에서 관리되는 2번쨰 공식 프로젝트
- Pull 방식의 구조와 다양한 Metric Exporter 제공
- 시계열 DB에 Metrics 저장 → Query(조회) 가능

### 2. Grafana 소개

- 데이터 시각화, 모니터링 및 분석을 위한 오픈소스 애플리케이션
- 시계열 데이터를 시각화하기 위한 대시보드 제공

### 3. Prometheus, Grafana 설치

#### Docker Compose 파일 작성

> docker-compose-prometheus-grafana.yml

```yaml
version: '3.8'
services:
  prometheus:
    image: prom/prometheus
    container_name: prometheus
    volumes:
      - ./prometheus/config:/etc/prometheus
      - ./prometheus/volume:/prometheus
    ports:
      - "9090:9090"
    command:
      - "--web.enable-lifecycle"
      - '--config.file=/etc/prometheus/prometheus.yml'
    networks:
      - promnet # 네트워크를 promnet로 설정

  grafana:
    image: grafana/grafana
    container_name: grafana
    ports:
      - "6300:3000"
    volumes:
      - ./grafana/volume:/var/lib/grafana
    networks:
      - promnet # promnet 네트워크에 연결

networks:
  promnet:
    driver: bridge # bridge 네트워크 사용
```

<br/> 

#### prometheus.yml 파일 작성

위치 : `{docker-compose.prometheus.yml 경로}/prometheus/config`

```yaml
global:
  scrape_interval: 15s
  evaluation_interval: 15s

# AlterManager
alerting:
  alertmanagers:
    - static_configs:
        - targets:

rule_files:
#  - "rule.yml"

scrape_configs:
  - job_name: 'prometheus'
    static_configs:
      - targets: [ 'localhost:9090' ]

  - job_name: 'apigateway-service'
    scrape_interval: 15s
    metrics_path: /actuator/prometheus
    static_configs:
      #      - targets: [ 'localhost:8000' ] # API 서버는 Docker 로 기동하지 않으므로, 직접 IP 혹은 DNS 명시.
      - targets: [ '192.168.0.2:8000' ] # API Gateway 주소

  - job_name: 'user-service'
    scrape_interval: 15s
    metrics_path: /user-service/actuator/prometheus
    static_configs:
      - targets: [ '192.168.0.2:8000' ] # API Gateway 주소
```

<br/>

#### Prometheus & Grafana 실행

```shell
docker-compose -f ../docker/docker-compose-prometheus-grafana.yml up
```

<br/>

#### Prometheus & Grafana 접속

- Prometheus : http://localhost:8080
- Grafana : http://localhost:6300
    - 초기 비밀번호 : admin/admin

<br/>

#### 이후 작업

1. Grafana DataSource 연결
2. Grafana Dashboard 연결 (metric 이름이 다를 경우는 직접 수정 필요)
3. 필요시 알람 설정 추가

<br/>

#### 최종 작업본.

![grafana-01.png](image/grafana-01.png)

<br/>

# 10. 배포를 위한 컨테이너 가상화

## 10-1. Virtualization

### 가상화 개념 소개

- 물리적인 컴퓨터 리소스를 다른 시스템이나 애플리케이션에서 사용할 수 있도록 제공
    - 플랫폼 가상화
    - 리소스 가상화
- 하이퍼바이저(`Hypervisor`)
    - Virtual Machine Manager(`VMM`)
    - 다수의 운영체제를 동시에 실행하기 위한 논리적 플랫폼
    - Type 1: `Native or Bare-metal`
        - 하드웨어에 직접 하이퍼바이저를 설치 후, 여러 OS를 구성
    - Type 2: `Hosted`
        - 하드웨어에 OS를 설치 후, 그 위에 Hypervisor 기능을 사용할 수 있는 Software를 설치
        - Docker 는 Hosted에 해당
- OS Virtualization
    - Host OS 위에 Guest OS 전체를 가상화
    - VMWare, VirtualBox
    - 자유도가 높으나, 시스템에 부하가 많고 느려짐
- Container Virtualization
    - Host OS가 가진 리소스를 적게 사용하며, 필요한 프로세스 실행
    - 최소한의 라이브러리와 도구만을 포함
    - Container의 생성 속도 빠름

<br/>

### 컨테이너 관련 개념 설명

- `Container Image`
    - 컨테이너 실행에 필요한 설정 값.
    - Image를 가지고 실체화할 경우 컨테이너에 해당.
- `Public Registry`
    - [Docker hub](https://hub.docker.com/)에 해당.
    - 공개된 저장소에 이미지를 저장해놓는 방식.
    - 마찬가지로 Private Registry도 존재.
- `Docker Host`
    - 컨테이너 서버 자체를 의미.
    - 여러 이미지를 명령어를 통해 컨테이너로 기동.
        - create : 컨테이너 생성
        - start : 컨테이너 실행
        - run : Registry로부터 이미지를 다운받은 후, create & start.

<br/>

### Dockerfile

- Docker Image를 생성하기 위한 스크립트 파일
- 자체 DSL(Domain-Specific Language) 언어 사용 → 이미지 생성과정을 기술

    ```dockerfile
    # 이미지를 생성할 대상 서버
    FROM mysql:5.7 
    
    # 필요한 환경변수
    ENV MYSQL_ALLOW_EMPTY_PASSWORD true
    ENV MYSQL_DATABASE mydb
    
    # 로컬과 이미지의 볼륨을 마운트
    ADD ../db_mount /var/lib/mysql
    
    # 외부에 오픈할 포트
    EXPOSE 3306
    
    # 이미지가 실행된 다음 최종적으로 실핼할 명령어
    CMD ["mysqld"]
    ```

<br/>

## 10-2. Docker

### Docker Desktop 설치 링크

- https://docs.docker.com/desktop/setup/install/mac-install/

![img](image/docker-desktop-01.png)

### Docker Command 맛보기

1. 설치된 도커 정보 조회

```shell
$ docker info

Client:
 Context:    default
 Debug Mode: false
 ...

Server:
 Containers: 3
  Running: 0
  Paused: 0
  Stopped: 3
 Images: 25
 Server Version: 20.10.21
 ...
```

2. 설치된 이미지 목록 조회

```shell
$ docker image ls

REPOSITORY                      TAG            IMAGE ID       CREATED         SIZE
openzipkin/zipkin               latest         31b486c2db0e   5 weeks ago     186MB
ngrinder-agent-ngrinder-agent   latest         6c0c2c76f99f   2 months ago    498MB
ngrinder-ngrinder_agent         latest         ffb4ae04b6ca   2 months ago    498MB
ngrinder-ngrinder-controller    latest         64ed23b0dc02   2 months ago    573MB
ubuntu/mysql                    edge           f546ffb82146   3 months ago    347MB
openzipkin/zipkin-slim          latest         6af41204b540   5 months ago    149MB
redis                           7.2-alpine     ed518de838a3   5 months ago    42MB
rabbitmq                        3-management   3a04ce433a99   8 months ago    235MB
prom/prometheus                 latest         afd08f0aa7c0   13 months ago   240MB
grafana/grafana                 latest         0a3c9111b0d7   13 months ago   381MB
mysql                           8.0            a5e6f938c138   16 months ago   587MB
wurstmeister/kafka              2.12-2.5.0     0fbb91a7ef0b   2 years ago     498MB
wurstmeister/zookeeper          latest         3f43f72cb283   5 years ago     510MB
rmohr/activemq                  latest         7c58d2d8d6af   6 years ago     569MB
```

3. 기동중인 컨테이너 목록 조회

```shell
$ docker container ls

CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
```

<br/>

## 3. Docker 컨테이너 실행하기

### 컨테이너 실행

```shell
$ docker run [OPTIONS] IMAGE[:TAG|@DIGEST] [COMMAND][ARG...]
# 옵션 예시
# -d : 백그라운드 모드(detached mode)
# -p : 호스트와 컨테이너의 포트를 연결
# -v : 호스트와 컨테이너의 디렉토리를 연결(마운트)
# -e : 컨테이너 내에서 사용할 환경변수 설정
# --name : 컨테이너 이름 설정
# --rm : 프로세스 종료시 컨테이너 자동 제거
# -it :  -i와 -t를 동시에 사용한 것으로, 터미널 입력을 위함
```

<br/>

## 4. Docker 이미지 생성 및 Registry Push

### Dockerfile 작성

```dockerfile
FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp

COPY target/user-service-1.0.0.jar UserService.jar

ENTRYPOINT ["java", "-jar", "UserService.jar"]

```

### Dockerfile 을 이용해 이미지 빌드

```shell
$ docker build --tag leedongyeop/user-service:1.0.0 . 

# docker image 빌드 결과 확인
$ docker image ls
```

### Docker Hub Registry Push

```shell
$ docker push leedongyeop/user-service:1.0.0    
```

### Docker Pull

```shell
$ docker pull leedongyeop/user-service:1.0.0
```

<br/>

# 11. 애플리케이션 배포 구성

![application-architecture-01.png](image/application-architecture-01.png)

## 11-1. Docker Network 구성

### 네트워크 종류

1. Bridge Network
    - `$ docker network create --driver bridge [브릿지 이름]`
    - 호스트와 별도로 가상의 네트워크를 의미.
    - **같은 네트워크에 속한 컨테이너의 경우는 IP 주소 외에 컨테이너 ID/Name 으로도 통신 가능.**
2. Host Network
    - 네트워클르 호스트로 설정시, 호스트의 네트워크를 그대로 사용
    - 포트 포워딩 없이 내부 애플리케이션을 사용
3. None Network
    - 네트워크를 사용하지 않음.
    - IO 네트워크만 사용 = 외부와 단절

### 네트워크 작업

```shell
# Docker 시스템 중 불필요한 리소스 삭제
$ docker system prune
WARNING! This will remove:
  - all stopped containers
  - all networks not used by at least one container
  - all dangling images
  - all dangling build cache
  
# 네트워크 조회
$ docker network ls
NETWORK ID     NAME      DRIVER    SCOPE
e48867f99790   bridge    bridge    local
222116d6dedd   host      host      local
687781e53556   none      null      local

# 네트워크 생성
# Gateway & Subnet 을 지정하지 않을 경우, 나중에 직접 IP를 할당시 에러 발생할 수도 있음.
# 따라서 수동으로 지정하는 것을 추천.
$ docker network create --gateway 172.18.0.1 --subnet 172.18.0.0/16 ecommerce-network

# 특정 네트워크에 대해 자세히 조회
# docker network inspect {네트워크명}
$ docker network inspect ecommerce-network
```

### 설계 목표

- 아래와 같이 동일한 Docker Network 상에 서버들을 Docker Container로 구성
- 각각의 서버들은 동일한 Docker Network에 위치하므로, IP 주소 외에도 Container 이름으로 참조 가능.
  ![application-architecture-02.png](image/application-architecture-02.png)

<br/>

## 11-2. 각 서비스들 가상화

### RabbitMQ

```shell
$ docker run -d --name rabbitmq --network ecommerce-network \
 -p 5672:5672 -p 15672:15672 -p 4369:4369 -p 5671:5671 -p 15671:15671 \
 -e RABBITMQ_DEFAULT_USER=guest -e RABBITMQ_DEFAULT_PASS=guest \
 rabbitmq:management
```

### Config Server

1. Dockerfile 작성

```shell
FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp

# Config Server의 경우는 jks 키파일도 반드시 복사해줘야함.
COPY keystore/2dongyeop.jks 2dongyeop.jks
COPY target/config-service-1.0.0.jar ConfigServer.jar

ENTRYPOINT ["java", "-jar", "ConfigServer.jar"]
```

2. Docker Image Build

```shell
# Config Server 위치에서
$ docker build --tag leedongyeop/config-service:1.0.0 .

$ docker push leedongyeop/config-service:1.0.0
```

3. Docker Container Start

- RabbitMQ의 주소를 (Docker network 내에서 실행중이므로) Docker Image Name으로 참조 가능.
    - `docker network inspect ecommerce-network` 명령어로 이름 및 주소 참고
- 또한 더이상 Local Config Repository를 사용하지 않고, Git을 Config Repository로 사용하기 위해 프로필을 Default로 지정.

```shell
$ docker run -d -p 8888:8888 --network ecommerce-network \
-e "spring.rabbitmq.host=rabbitmq" \
-e "spring.profiles.active=default" \
--name config-service leedongyeop/config-service:1.0.0
```

4. 기동 결과 확인

```shell
$ docker logs config-service
```

5. 설정파일 소스 확인

- 브라우저에서 `localhost:8888/ecommerce/default` 접속
- `propertySources.name`이 Git 주소로 되어있는지 확인

<br/>

### Discovery Service

1. Dockerfile 작성

```dockerfile
FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp

COPY target/discoveryservice-1.0.0.jar DiscoveryService.jar

ENTRYPOINT ["java", "-jar", "DiscoveryService.jar"]
```

2. Docker Image Build

```shell
$ docker build --tag leedongyeop/discovery-service:1.0.0 .

# Docker Image Push To Public Repository
$ docker push leedongyeop/discovery-service:1.0.0
```

3. Docker Container Start

- Config Server의 주소명에 IP 대신 Docker Image Name으로 명시

```shell
$ docker run -d -p 8761:8761 --network ecommerce-network \
 -e "spring.cloud.config.uri=http://config-service:8888" \
 --name discovery-service leedongyeop/discovery-service:1.0.0
```

<br/>

### API Gateway Service

1. Dockerfile 작성

```dockerfile
FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp

COPY target/apigateway-service-1.0.0.jar ApiGatewayService.jar

ENTRYPOINT ["java", "-jar", "ApiGatewayService.jar"]
```

2. Docker Image Build

```shell
$  docker build --tag leedongyeop/apigateway-service:1.0.0 .

# Docker Image Push To Public Repository
$ docker push leedongyeop/apigateway-service:1.0.0
```

3. Docker Container Start

- 각 Server의 주소명에 IP 대신 Docker Image Name으로 명시
    - Config Server, Discovery, RabbitMQ Host

```shell
docker run -d -p 8000:8000 --network ecommerce-network \
 -e "spring.cloud.config.uri=http://config-service:8888" \
 -e "spring.rabbitmq.host=rabbitmq" \
 -e "eureka.client.serviceUrl.defaultZone=http://discovery-service:8761/eureka/" \
 --name apigateway-service \
 leedongyeop/apigateway-service:1.0.0
```

<br/>

### MariaDB

1. Dockerfile 작성

```dockerfile
FROM mariadb
ENV MYSQL_ROOT_PASSWORD test1234
ENV MYSQL_DATABASE mydb

COPY ./mysql /var/lib/mysql

EXPOSE 3306

ENTRYPOINT ["mysqld", "--user=root"]
```

2. Docker Image Build

```shell
$ docker build --tag leedongyeop/my-mariadb:1.0.0 .

# Docker Image Push To Public Repository
$ docker push leedongyeop/my-mariadb:1.0.0
```

3. Docker Container Start

```shell
docker run -d --name mysql-container \
  --network ecommerce-network \
  --restart always \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=test1234 \
  -e TZ=Asia/Seoul \
  -v $(pwd)/mysql/data:/var/lib/mysql \
  mysql:8.0 \
  --character-set-server=utf8mb4
```

4. MySQL Container 접속 권한 변경

- 어느 IP에서 접근하더라도 모두 접근가능하도록 설정

```shell


$ docker exec -it mysql-container /bin/bash

$ mysql -hlocalhost -uroot -ptest1234 

$ GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';

```

<br/>

### Zookeeper + Kafka Standalone

1. Docker Compose 파일 작성

```yaml
version: '2'
services:
  zookeeper:
    image: wurstmeister/zookeeper
    ports:
      - "2181:2181"
    networks:
      my-network:
        ipv4_address: 172.18.0.100
  kafka:
    # build: .
    image: wurstmeister/kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_ADVERTISED_HOST_NAME: 172.18.0.101
      KAFKA_CREATE_TOPICS: "test:1:1"
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
    depends_on:
      - zookeeper
    networks:
      my-network:
        ipv4_address: 172.18.0.101

networks:
  my-network:
    name: ecommerce-network # 172.18.0.1(Gateway) ~ ...
```

2. Docker Compose 실행

```shell
$ docker-compose -f docker-compose-single-broker.yml up -d
```

<br/>

### Zipkin

```shell
$ docker run -d -p 9411:9411 --network ecommerce-network \
  --name zipkin openzipkin/zipkin 
```

<br/>

### Prometheus & Grafana

1. Prometheus.yml target 정보 수정

```yaml
scrape_configs:
  - job_name: 'prometheus'
    static_configs:
      - targets: [ 'prometheus:9090' ]

  - job_name: 'apigateway-service'
    scrape_interval: 15s
    metrics_path: /actuator/prometheus
    static_configs:
      - targets: [ 'apigateway-service:8000' ] # localhost, IP 주소가 아닌 Docker 컨테이너 이름으로 명시
```

2. Docker 명령어 실행

```shell
# Prometheus
$ docker run -d -p 9090:9090 \
 --network ecommerce-network \
 --name prometheus \
 -v /Users/2dongyeop/Developments/spring-cloud-msa/docker/prometheus/config/prometheus.yml:/etc/prometheus/prometheus.yml \
 prom/prometheus 

# Grafana
$ docker run -d -p 3000:3000 \
 --network ecommerce-network \
 --name grafana \
 grafana/grafana 
```

<br/>

### User Service

1. Dockerfile 작성

```dockerfile
FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp

COPY target/user-service-1.0.0.jar UserService.jar

ENTRYPOINT ["java", "-jar", "UserService.jar"]
```

2. Docker Container Start

```shell
docker run -d --network ecommerce-network \
  --name user-service \
  -e "spring.cloud.config.uri=http://config-service:8888" \
  -e "spring.rabbitmq.host=rabbitmq" \
  -e "spring.zipkin.base-url=http://zipkin:9411" \
  -e "management.zipkin.tracing.endpoint=http://zipkin:9411/api/v2/spans" \
  -e "eureka.client.serviceUrl.defaultZone=http://discovery-service:8761/eureka/" \
  -e "logging.file=/api-logs/users-ws.log" \
  leedongyeop/user-service:1.0.0
```

<br/>

### Order Service

1. Dockerfile 작성

```dockerfile
FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp

COPY target/order-service-1.0.0.jar OrderService.jar

ENTRYPOINT ["java", "-jar", "OrderService.jar"]
```

2. Docker Image Build

```shell
$ docker build --tag leedongyeop/order-service:1.0.0 .
$ docker push leedongyeop/order-service:1.0.0
```

3. Docker Container Start

```shell
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
```

<br/>

1. Dockerfile 작성

```dockerfile
FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp

COPY target/catalog-service-1.0.0.jar CatalogService.jar

ENTRYPOINT ["java", "-jar", "CatalogService.jar"]
```

2. Docker Image Build

```shell
$ docker build --tag leedongyeop/catalog-service:1.0.0 .
$ docker push leedongyeop/catalog-service:1.0.0
```

3. Docker Container Start

```shell
docker run -d --network ecommerce-network \
  --name catalog-service \
  -e "spring.datasource.url=jdbc:mysql://mysql-container:3306/mydb?useSSL=false&serverTimezone=Asia/Seoul&autoReconnect=true&useUnicode=yes&characterEncoding=UTF-8&allowPublicKeyRetrieval=true" \
  -e "kafka.server.ip=172.18.0.101:9092" \
  -e "eureka.client.service-url.defaultZone=http://discovery-service:8761/eureka/" \
  -e "logging.file=/api-logs/catalogs-ws.log" \
  leedongyeop/catalog-service:1.0.0
```

<br/>

# 12. Microservice Patterns

## 12-1. Event Sourcing

![event-sourcing.png](image/event-sourcing.png)

### 개념 정리

- 데이터의 마지막 상태만 저장하는 것이 아닌, 해당 데이터에 수행된 전체 이력을 기록
- 데이터 구조 단순, 데이터의 일관성과 트랜잭션 처리 가능
- 데이터의 저장소의 개체를 직접 업데이트하지 않기 때문에, 동시성에 대한 충돌 문제 해결
- 메시지 중심의 비동기 작업 처리

### 도메인 주도 설계 (Domain-Driven Design)

- Aggregate
    - 데이터의 상태를 바꾸기 위한 개념
- Projection
    - 데이터의 상태값을 표현하는 개념

### 단점

- 모든 이벤트를 기록하기 때문에, 모든 트랜잭션을 복원하기에 시간이 걸림.
    - **스냅샷**이라는 개념을 도입하여, 트랜잭션을 나누어서 관리해야 함.
- 다양한 데이터가 여러번 조회
    - **CQRS** 를 이용.

<br/>

## 12-2. CQRS (Command and Query Responsibility Segregation)

![cqrs.png](image/cqrs.png)

### 개념 정리

- 명령과 조회의 책임을 분리하는 개념
- 명령 : Command & 조회 : Query

<br/>

## 12-3. Saga Pattern

![saga-pattern.png](image/saga-pattern.png)

### 개념 정리

- Application에서 Transaction 처리
    - Choreography, Orchestration
- Application이 분리된 경우에 각각의 로컬 Transaction만 처리
- 각 App에 대한 연속적인 Transaction에서 실패할 경우
    - Rollback 처리 구현 → `보상 Transaction` (이전으로 롤백하는 작업.)
- 데이터의 원자성을 보장하지는 않지만, 일관성을 보장

<br/>

### Choreography-based Saga

![Choreography-based-Saga.png](image/Choreography-based-Saga.png)

> 소개

- 여러 서비스가 서로 협력하여 일을 진행하는 방식
- 각 서비스는 자신이 해야 할 일을 끝낸 후, 다음에 해야 할 일을 다른 서비스에 알려주거나 신호를 보냄

<br/>

> 예시

1. 고객이 항공권을 예약합니다.
2. 항공권 예약 서비스가 예약 완료 후, "항공권 예약 성공" 이벤트를 발행합니다.
3. 호텔 예약 서비스는 이 이벤트를 듣고 호텔 예약을 시도합니다.
4. 호텔 예약이 성공하면 또 다른 이벤트를 발행합니다.
5. 결제 서비스가 이를 듣고 결제를 처리합니다.

<br/>

> 장점

- 각 서비스가 독립적으로 동작하기 때문에 느슨하게 결합
- 서비스 간 의존도가 낮아서 변경 사항을 적용하기 쉬움

<br/>

> 단점

- 이벤트 흐름이 복잡해질 수 있으며, 누락되거나 잘못된 이벤트가 발생하면 추적이 어려움
- 전체 프로세스의 흐름을 파악하기가 어려울 수 있음

<br/>

### Orchestration-based Saga

![Orchestration-based-Saga.png](image/Orchestration-based-Saga.png)


> 소개

- 한 중앙 관리자가 모든 작업을 지휘하는 방식
- "오케스트라 지휘자"처럼 중앙 역할을 하는 서비스가 전체 흐름을 계획하고, 각 서비스에게 명령을 내림

<br/>

> 예시

1. 고객이 항공권을 예약합니다.
2. 중앙 관리 서비스(오케스트레이터)가 항공권 예약 서비스에 "예약을 시작하세요"라고 요청합니다.
3. 예약이 완료되면 오케스트레이터는 호텔 예약 서비스에 "이제 호텔을 예약하세요"라고 지시합니다.
4. 모든 작업이 성공하면 오케스트레이터가 결제 서비스에 "결제를 완료하세요"라고 명령합니다.

<br/>

> 장점

- 중앙 관리자가 있어 전체 흐름을 명확하게 볼 수 있음.
- 문제 발생 시 중앙에서 오류를 쉽게 처리하거나 조정 가능.

<br/>

> 단점

- 중앙 관리자가 모든 작업을 처리하므로, 이 관리자가 복잡해지고 병목 지점이 될 수 있음.
- 중앙 관리자가 다운되면 전체 프로세스가 멈출 위험이 존재.

<br/>

### 최종 선택

- 서비스 간의 독립성과 유연성이 중요하다면 → Choreography
- 명확한 제어와 관리가 중요하다면 → Orchestration

<br/>

# 13. Spring Boot 3.2 + Spring Cloud 2023.

위에서 진행한 내용들이 이미 모두 Spring 3.3 + Spring Cloud 2023 으로 구성되어 있기에 큰 변화는 없습니다.

## 13-1. Eureka Service 이중화

### Eureka Service 설정파일 수정

```yaml
server:
  port: 8761

spring:
  application:
    name: discoveryservice

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false

---

## Eureka Server를 여러 대 기동시에 대한 설정.
## eureka2 는 eureka3을 참조하고, eureka3은 eureka2를 참조하게 하여 등록된 서버 정보를 공유하도록 함.

spring:
  config:
    activate:
      on-profile: eureka2

server:
  port: 8762

eureka:
  client:
    serviceUrl:
      defaultZone: http://idong-yeob-ui-MacBookAir.local:8763/eureka/  # Terminal 에서 $ hostname 명령어 입력시 나오는 값
  instance:
    hostname: localhost

---

spring:
  config:
    activate:
      on-profile: eureka3

server:
  port: 8763

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8762/eureka/
  instance:
    hostname: DOWONui-MacBookPro.local
```

<br/>

### 기타 API Server 설정파일 수정

```yaml
server:
  port: 0

spring:
  application:
    name: my-first-service

eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url: # 이중화된 2개 이상의 Eureka 서버를 지정.
      defaultZone: http://idong-yeob-ui-MacBookAir.local:8763/eureka, http://localhost:8763/eureka
  instance:
    instance-id: ${spring.application.name}:${spring.application.instance_id:${random.value}}
```

<br/>

### 이중화 효과

- API Server는 Eureka Server 2대에 등록됨
    - 여러 Eureka Server 끼리 등록되어 있는 서버 정보를 공유
- 2대 중 1대가 문제가 생겨 종료되고 재기동했을 때, 다른 유레카 서버로부터 정보를 공유받을 수 있음.

<br/>

## 13-2. Gateway 를 통한 요청인지 구분 추가

```yaml
spring:
  cloud:
    gateway:
      default-filters:
        - name: GlobalFilter
          args:
            baseMessage: Spring Cloud Gateway Global Filter
            preLogger: true
            postLogger: true
      routes:
        - id: first-service
          uri: lb://MY-FIRST-SERVICE
          predicates:
            - Path=/first-service/**
          filters:
            - AddRequestHeader=first-request, first-request-header-by-yaml
            - AddResponseHeader=first-response, first-response-header-by-yaml
            - CustomFilter
        - id: second-service
          uri: lb://MY-SECOND-SERVICE
          predicates:
            - Path=/second-service/**
          filters:
            - AddRequestHeader=second-request, second-request-header-by-yaml
            - AddResponseHeader=second-response, second-response-header-by-yaml
            - name: CustomFilter
            - name: LoggingFilter
              args:
                baseMessage: Hi, Logging Filter
                preLogger: true
                postLogger: true
```

<br/>

# 14. Kubernetes 배포

## 14.1 배포 목표 형태

| 구분                 | Before           (Docker)  | After (k8s)                        |
|--------------------|----------------------------|------------------------------------|
| Outer Architecture | Eureka(Discovery Service)  | service                            |
| Outer Architecture | Spring Cloud Gateway       | service / ingress                  |
| Outer Architecture | Spring Cloud Config Server | configmap / secret                 |
| Outer Architecture | Kafka                      | Kafka                              |
| Inner Architecture | user-service               | deployment / pod <user-service>    |
| Inner Architecture | order-service              | deployment / pod <order-service>   |
| Inner Architecture | catalog-service            | deployment / pod <catalog-service> |

<br/>

## 14.2 k8s ConfigMap 동작 개념

1. `k8s/configmap.yml` 에 환경변수 값 설정

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: msa-k8s-configmap
data:
  gateway_ip: 192.168.0.1
  ...
```

2. `k8s/user-deploy.yml` 에서 `configmap.yml`을 참조

```yaml
...
env:
  - name: GATEWAY_IP
    valueFrom:
    configMapKeyRef:
      name: msa-k8s-configmap
      key: gateway_ip
```

3. 각 API 서버들의 환경 설정 파일(`application.yml`) 에서 `user-deploy.yml`을 참조

```yaml
gateway:
  ip: ${GATEWAY_IP}
```

<br/>

## 14.3 k8s Kafka 환경 구성

- 아래의 `KAFKA_ADVERTISED_LISTENERS`에서 입력하는 `PLAINTEXT_HOST` 속성은 Kafka가 접근을 허용할 주소에 해당됨.
    - 다음 명령어 참조 - `kubectl describe node docekr-desktop | grep InternalIP`

```yaml
# k8s/docker-compose-kafka.yml
services:
  broker:
    image: apache/kafka:latest
    hostname: broker
    container_name: broker
    ports:
      - '9092:9092'
    environment:
      KAFKA_NODE_ID: 1
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: 'CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT'
      KAFKA_ADVERTISED_LISTENERS: 'PLAINTEXT_HOST://192.168.65.3:9092,PLAINTEXT://broker:19092'
      KAFKA_PROCESS_ROLES: 'broker,controller'
      KAFKA_CONTROLLER_QUORUM_VOTERS: '1@broker:29093'
      KAFKA_LISTENERS: 'CONTROLLER://:29093,PLAINTEXT_HOST://:9092,PLAINTEXT://:19092'
      KAFKA_INTER_BROKER_LISTENER_NAME: 'PLAINTEXT'
      KAFKA_CONTROLLER_LISTENER_NAMES: 'CONTROLLER'
      CLUSTER_ID: '4L6g3nShT-eMCtK--X86sw'
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS: 0
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
      KAFKA_LOG_DIRS: '/tmp/kraft-combined-logs'
```

## 14.4 k8s 명령어

### 배포 관련

```shell
# k8s node 정보 확인
$ kubectl get nodes
NAME             STATUS   ROLES           AGE     VERSION
docker-desktop   Ready    control-plane   4m32s   v1.25.2

# k8s node IP Address 확인
$ kubectl describe node docker-desktop | grep InternalIP
InternalIP:  192.168.65.4

# Kafka 실행 (아래에서 docker-compose-kafka.yml 참고)
$ docker-compose up -f docker-compose-kafka.yml -describe

# k8s deployment, service 실행
$ kubectl apply -f k8s/user-deploy.yml
$ kubectl apply -f k8s/order-deploy.yml
$ kubectl apply -f k8s/catalog-deploy.yml
```

### 실행 관련

```shell
# docker kafka 실행
$ docker-compose -f docker-compose-kafka.yml up -d

# docker container process 확인
$ docker-compose -f docker-compose-kafka.yml ps
NAME                COMMAND                  SERVICE             STATUS              PORTS
broker              "/__cacert_entrypoin…"   broker              running             0.0.0.0:9092->9092/tcp

# 서비스 기동 상태 확인
$ kubectl get svc

# deploy 상태 확인
$ kubectl get deploy

# pod 상태 확인
$ kubectl get pod
```

## 14.5 k8s 인프라 구성

### ConfigMap 설정파일 작성

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: msa-k8s-configmap
data:
  gateway_ip: "192.168.65.4"  # k8s node Internal IP
  token_expiration_time: "86400000"
  token_secret: "SAMPLE_KEY_#1"
  order-service-url: "http://order-service:10000"
  bootstrap-servers: "192.168.65.3:9092"
```

### ConfigMap 실행

```shell
$ 실행
$ kubectl apply -f configmap.yml
configmap/msa-k8s-configmap created

# 실행 확인
$ kubectl get configmap
NAME                DATA   AGE
kube-root-ca.crt    1      2d3h
msa-k8s-configmap   5      38s
```

### API Server Build

```shell
# user-service 위치에서 Jar file build
$ mvn clean compile package -DskipTests=true

# Docker Image Build
$ docker build --tag leedongyeop/user-service:k8s_v1.0 -f Dockerfile .

# Docker Hub Push
$ docker push leedongyeop/user-service:k8s_v1.0

# order-service & catalog-service 도 동일하게 위 과정 수행.
```

### User Service Deploy Yaml 작성

```yaml
# user-deploy.yml
apiVersion: apps/v1
kind: Deployment  # 쿠버네티스 리소스 중 deployment 임을 명시.
# k8s에서는 Pod 단위로 서비스를 운영 & Pod 내에는 여러 컨테이너가 존재
# Pod를 묶어서 배포 단위로 사용할 때, 이 deployment라는 리소스를 사용
metadata:
  name: user-deploy
spec:
  selector:
    matchLabels:
      app: user-app
  replicas: 1
  template:
    metadata:
      labels:
        app: user-app
    spec:
      containers:
        - name: user-service
          image: leedongyeop/user-service:k8s_v1.0 # Docker Hub Image
          imagePullPolicy: Always # 매번 배포할 때마다 새롭게 Hub에서 pull
          ports:
            - containerPort: 60000
              protocol: TCP
          resources:
            requests: # Pod (컨테이너)가 사용할 수 있는 리소스
              cpu: 500m
              memory: 1000Mi
          env: # user-service의 application.yml에서 참조할 환경설정 값들
            - name: GATEWAY_IP
              valueFrom:
                configMapKeyRef:
                  name: msa-k8s-configmap # 참조할 ConfigMap Name
                  key: gateway_ip         # 참조할 값의 Key
            - name: TOKEN_EXPIRATION_TIME
              valueFrom:
                configMapKeyRef:
                  name: msa-k8s-configmap
                  key: token_expiration_time
            - name: TOKEN_SECRET
              valueFrom:
                configMapKeyRef:
                  name: msa-k8s-configmap
                  key: token_secret
            - name: ORDER-SERVICE-URL
              valueFrom:
                configMapKeyRef:
                  name: msa-k8s-configmap
                  key: order-service-url
---
apiVersion: v1
kind: Service  # 쿠버네티스 리소스 중 service 임을 명시.
# k8s에서 외부에서 deployment나 pod를 사용하기 위해 필요한 네트워크 설정을 담당. 
metadata:
  name: user-service
spec:
  type: NodePort  # 호스트 PC에서 사용할 수 있도록 노드 포트로 지정하는 옵션
  selector:
    app: user-app # deployment에서 명시한 값과 동일해야 지정됨.
  ports:
    - protocol: TCP
      port: 60000  # Spring Boot Port(60000)을 외부에서 접근할 때는 30001로 관리
      targetPort: 60000
      nodePort: 30001
```

### Order Service Deploy Yaml 작성

```yaml
# order-deploy.yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: order-deploy
spec:
  selector:
    matchLabels:
      app: order-app
  replicas: 1
  template:
    metadata:
      labels:
        app: order-app
    spec:
      containers:
        - name: order-service
          image: leedongyeop/order-service:k8s_v1.0
          imagePullPolicy: Always
          ports:
            - containerPort: 10000
              protocol: TCP
          resources:
            requests:
              cpu: 500m
              memory: 1000Mi
          env:
            - name: BOOTSTRAP-SERVERS
              valueFrom:
                configMapKeyRef:
                  name: msa-k8s-configmap
                  key: bootstrap-servers
---
apiVersion: v1
kind: Service
metadata:
  name: order-service
spec:
  type: NodePort
  selector:
    app: order-app
  ports:
    - protocol: TCP
      port: 10000
      targetPort: 10000
      nodePort: 30002
```

### Catalog Service Deploy Yaml 작성

```yaml
# catalog-deploy.yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: catalog-deploy
spec:
  selector:
    matchLabels:
      app: catalog-app
  replicas: 1
  template:
    metadata:
      labels:
        app: catalog-app
    spec:
      containers:
        - name: catalog-service
          image: leedongyeop/catalog-service:k8s_v1.0
          imagePullPolicy: Always
          ports:
            - containerPort: 8080
              protocol: TCP
          resources:
            requests:
              cpu: 500m
              memory: 1000Mi
          env:
            - name: BOOTSTRAP-SERVERS
              valueFrom:
                configMapKeyRef:
                  name: msa-k8s-configmap
                  key: bootstrap-servers
---
apiVersion: v1
kind: Service
metadata:
  name: catalog-service
spec:
  type: NodePort
  selector:
    app: catalog-app
  ports:
    - protocol: TCP
      port: 8080
      targetPort: 8080
      nodePort: 30003
```

### k8s 적용

```shell
# order & catalog 서비스도 동일하게 apply
$ kubectl apply -f k8s/user-deploy.yml
deployment.apps/user-deploy created
service/user-service created

# 서비스 조회
$ kubectl get svc
NAME              TYPE        CLUSTER-IP     EXTERNAL-IP   PORT(S)           AGE
catalog-service   NodePort    10.97.196.18   <none>        8080:30003/TCP    69s
kubernetes        ClusterIP   10.96.0.1      <none>        443/TCP           3d5h
order-service     NodePort    10.103.20.33   <none>        10000:30002/TCP   99s
user-service      NodePort    10.105.32.8    <none>        60000:30001/TCP   2m29s

# Deployment 조회
$ kubectl get deploy
NAME             READY   UP-TO-DATE   AVAILABLE   AGE
catalog-deploy   0/1     1            0           110s
order-deploy     0/1     1            0           2m20s
user-deploy      0/1     1            0           3m10s

# Pod 조회
### 아래와 같이 STATUS가 Running 으로 나와야 정상에 해당.
### 현재 예시는 DB & Gateway와 같이 필수적인 서비스들을 제외하고 단순 k8s 동작과정을 이해하기 위해 진행했기에 발생한 문제들.
$ kubectl get pod
NAME                              READY   STATUS             RESTARTS      AGE
catalog-deploy-66b9bd47bc-grq22   0/1     CrashLoopBackOff   3 (32s ago)   2m16s
order-deploy-86f8b67d47-fgfl7     1/1     Running            4 (59s ago)   2m46s
user-deploy-89897c9f4-jdx5s       0/1     ErrImagePull       0             3m36s

# 특정 Pod 로그 확인
# kubectl logs -f {Pod Name}
$ kubectl logs -f user-deploy-89897c9f4-jdx5s
```


