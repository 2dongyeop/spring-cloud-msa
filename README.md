
인프런에서 [Spring Cloud로 개발하는 마이크로서비스 애플리케이션 강의](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%81%B4%EB%9D%BC%EC%9A%B0%EB%93%9C-%EB%A7%88%EC%9D%B4%ED%81%AC%EB%A1%9C%EC%84%9C%EB%B9%84%EC%8A%A4/dashboard)를 듣고 정리한 내용입니다.

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
          args:                       # GlobalFilter에서 사용되는 Config의 속성 바인딩
            baseMessage: Spring Cloud Gateway Global Filter
            preLogger: true
            postLogger: true
      routes:
        - id: first-service
          uri: http://localhost:8081/
          filters:                    # 특정 마이크로 서비스에만 필터 적용시
            - name: CustomFilter
            - name: LoggingFilter
              args:                   # LoggingFilter에서 사용되는 Config의 속성 바인딩
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

- Config Server는 여러 서비스(MSA) 들의 설정 파일을 외부로 분리해, 하나의 중앙 설정 저장소 처럼 관리 할 수 있도록 해주며, **특정 설정 값이 변경시 각각의 서비스를 재기동 할 필요없이 적용이 가능하다.**
- 기본적으로 설정 정보 저장을 위해 git을 사용하도록 되어있어서 손쉽게 외부 도구들로 접근 가능하고, 버전 관리도 가능하다. (Git이 아닌, 서버에서 파일로 로컬 보관도 가능하다.)

<br/>

## 3.1 Spring Cloud Config의 장단점
### 장점
1. 여러 서버의 설정 파일을 중앙 서버에서 관리할 수 있다. 
2. 서버를 재배포 하지 않고 설정 파일의 변경사항을 반영할 수 있다.

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
- [참고자료](https://docs.spring.io/spring-cloud-config/reference/server/encryption-and-decryption.html)

Config Server는 속성을 암호화하기 위해 대칭 키와 비대칭 키를 모두 지원한다.

### 대칭키로 속성 암호화
```yaml
encrypt:
  key: example
```

### 비대칭 키로 속성 암호화
RSA나 키스토어를 참조하는 방식을 말하며, 아래에서는 keytool 명령어를 설명한다.
아래 명령어를 실행할 경우, keystore.jks 파일이 생성된다.
```shell
keytool -genkeypair -alias {key-name} -keyalg RSA \
-dname "CN=Web Server, OU=Unit, O=Organization, L=City, S=State, C=US" \
-keypass {password} -keystore keystore.jks -storepass {secret}
```

키  스토어를 애플리케이션 루트에 둘 경우, 아래와 같이 키스토어를 사용하도록 구성할 수 있다.
```yaml
encrypt:
  key-store:
    alias: { key-name }
    location: classpath:/keystore.jks
    password: { password }
    secret: { secret }
```

위처럼 키 스토어가 준비된 후에는 아래와 같이 속성을 암호화해보자.
```shell
$ curl {config-server-url}:{port}/encrypt -d "plaintext"
-> {cipher-text}
```

이후에는 위에서 암호화한 값들을 속성에 작성하면 된다.

## 3.4 Config Server의 변경 값을 Micro Service에 적용하기
### 1. Micro Service를 재부팅하기
이 방식은 서비스를 재부팅함으로써 다운타임이 생긴다는 치명적인 단점이 존재.

### 2. Spring Actuator Refresh 기능 이용하기
Spring Actuator의 Refresh 엔드포인트를 이용하면, 참조하는 환경 설정파일을 새로고침 한다.

따라서 API Server를 재시작하지 않고, 변경 사항을 적용할 수 있다.

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

### 3. Spring Cloud Bus 이용하기
추후 작성.