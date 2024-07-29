package io.dongvelop.apigatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApigatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApigatewayServiceApplication.class, args);
    }

    // Spring Boot 2.x에서 Http Trace를 위한 빈 등록
    // @Bean
//    public HttpTraceRepository httpTraceRepository() {
//        return new InMemoryHttpTraceRepository();
//    }
}
