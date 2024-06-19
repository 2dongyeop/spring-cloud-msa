
인프런에서 [Spring Cloud로 개발하는 마이크로서비스 애플리케이션 강의](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%81%B4%EB%9D%BC%EC%9A%B0%EB%93%9C-%EB%A7%88%EC%9D%B4%ED%81%AC%EB%A1%9C%EC%84%9C%EB%B9%84%EC%8A%A4/dashboard)를 듣고 정리한 내용입니다.

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