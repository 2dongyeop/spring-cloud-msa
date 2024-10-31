package io.dongvelop.userservice.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 10. 30
 * @description
 */
@Configuration
public class Resilience4JConfig {

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
