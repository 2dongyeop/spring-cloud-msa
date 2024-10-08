인프런에서 [Spring Cloud로 개발하는 마이크로서비스 애플리케이션 강의](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%81%B4%EB%9D%BC%EC%9A%B0%EB%93%9C-%EB%A7%88%EC%9D%B4%ED%81%AC%EB%A1%9C%EC%84%9C%EB%B9%84%EC%8A%A4/dashboard)
를 듣고 정리한 내용입니다.

<br/>

## 목차

- [Spring Cloud Netflix Eureka](#1-spring-cloud-netflix-eureka)
    - [Eureka Server](#11-eureka-server)
- [API Gateway(Netflix Zuul, Spring Cloud Gateway)](#2-api-gateway-netflix-zuul-spring-cloud-gateway)
    - [API Gateway란?](#21-api-gateway란)
    - [Spring Cloud Zuul](#22-spring-cloud-zuul)
    - [Spring Cloud Gateway](#23-spring-cloud-gateway)
    - [Spring Cloud Gateway Filter](#24-spring-cloud-gateway--filter-적용하기)
    - [Spring Cloud Gateway LoadBalancer](#24-spring-cloud-gateway--loadbalancer)
- [Spring Cloud Config Server](#3-spring-cloud-config-server)
    - [Spring Cloud Config 적용 방법](#31-spring-cloud-config-적용-방법)
    - [Spring Cloud Config Encrypt/Decrypt](#32-spring-cloud-config-적용-방법)
    - [Config Server의 변경사항을 종료없이 Micro Service에 적용하기](#34-config-server의-변경-값을-micro-service에-적용하기)
- [Spring Cloud Bus](#4-spring-cloud-bus)
    - [AMQP 설명](#41-amqp-설명)
    - [Spring Cloud Bus 동작](#42-spring-cloud-bus-동작-방식)
    - [Spring Cloud Bus 적용](#43-spring-cloud-bus-적용하기)
- [설정 정보의 암호화 처리](#5-설정-정보의-암호화-처리-)
    - [암복호화 기본 개념](#51-암호화-기본-개념)
    - [설정파일 암복호화 활성화](#52-설정파일-암복호화-활성화)

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

## 1.1 Eureka Server

- REST API 기반으로 다양한 언어에서 사용 가능
- 레지스트리의 모든 정보는 모든 Eureka Client에서 복제되어 있으며 가용 중인 모든 서비스들의 목록을 확인할 수 있고 30초마다 목록이 갱신됨
- Eureka Client들에게 자신이 가지고 있는 Eureka Client들의 정보를 공유
- 가용 상태의 서비스 목록 확인 시 서비스의 이름을 기준으로 탐색, 로드 밸런싱을 위해 내부적으로 Ribbon (클라이언트 측의 로드 밸런서) 을 사용

<br/>

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

## 1.2 Eureka Client
- 서비스 시작 시 Eureka Server에 자신의 정보를 등록
- 등록된 후 30초마다 레지스트리에 핑을 전송하여 자신의 가용 상태를 알림
- 레지스트리로부터 다른 Eureka Client의 서비스 정보를 확인할 수 있음

<br/>

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

## 2.1 API Gateway란?

![img](image/apigateway-1.png)

> API Gateway의 역할

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

## 2.4 Spring Cloud Gateway : LoadBalancer

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

## 2.4 Spring Cloud Gateway : Predicates & Filters

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

- Config Server는 여러 서비스(MSA) 들의 설정 파일을 외부로 분리해, 하나의 중앙 설정 저장소 처럼 관리 할 수 있도록 해주며, **특정 설정 값이 변경시 각각의 서비스를 재기동 할 필요없이 적용이
  가능하다.**
- 기본적으로 설정 정보 저장을 위해 git을 사용하도록 되어있어서 손쉽게 외부 도구들로 접근 가능하고, 버전 관리도 가능하다. (Git이 아닌, 서버에서 파일로 로컬 보관도 가능하다.)

<br/>

## 3.1 Spring Cloud Config의 장단점

### 장점

1. 여러 서버의 설정 파일을 중앙 서버에서 관리할 수 있다.2. 서버를 재배포 하지 않고 설정 파일의 변경사항을 반영할 수 있다.

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

## 3.2 Spring Cloud Config 적용 방법

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

## 3.3 Spring Cloud Config Encrypt/Decrypt

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

## 3.4 Config Server의 변경 값을 Micro Service에 적용하기

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

