package io.dongvelop.apigatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 18
 * @description
 */
//@Configuration
public class FilterConfig {

    /**
     * 라우팅 동작을 Java 코드로 작성한 예시. <br/>
     * 이를 설정 파일에서도 작성할 수 있음. <- 이 방법을 권장.
     */
//    @Bean
    public RouteLocator gatewayRoutes(final RouteLocatorBuilder builder) {
        return builder.routes()
                .route(route -> route
                        .path("/first-service/**") // 이 주소로 요청이 올 경우
                        .filters(filter -> filter      // 필터 동작을 실행
                                .addRequestHeader("first-request", "first-request-header")
                                .addResponseHeader("first-response", "first-response-header"))
                        .uri("http://localhost:8081") // 이후, 이 주소로 요청을 보냄
                )
                .route(route -> route
                        .path("/second-service/**")
                        .filters(filter -> filter
                                .addRequestHeader("second-request", "second-request-header")
                                .addResponseHeader("second-response", "second-response-header"))
                        .uri("http://localhost:8082")
                )
                .build();
    }
}
