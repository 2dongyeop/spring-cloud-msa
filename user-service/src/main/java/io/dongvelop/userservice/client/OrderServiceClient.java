package io.dongvelop.userservice.client;

import io.dongvelop.userservice.error.FeignErrorDecoder;
import io.dongvelop.userservice.vo.ResponseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 10. 14
 * @description
 */
@FeignClient(name = "order-service", configuration = FeignErrorDecoder.class, url = "${order-service-url}")
public interface OrderServiceClient {

    @GetMapping("/order-service/{userId}/orders")
    List<ResponseOrder> getOrders(@PathVariable String userId);
}
