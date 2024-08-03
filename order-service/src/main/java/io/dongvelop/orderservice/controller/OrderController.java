package io.dongvelop.orderservice.controller;

import io.dongvelop.orderservice.dto.OrderDto;
import io.dongvelop.orderservice.jpa.OrderEntity;
import io.dongvelop.orderservice.service.OrderService;
import io.dongvelop.orderservice.vo.RequestOrder;
import io.dongvelop.orderservice.vo.ResponseOrder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.MDC;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 27
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/order-service")
@RequiredArgsConstructor
public class OrderController {

    private final Environment env;
    private final OrderService orderService;

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@RequestBody RequestOrder orderDetails,
                                                     @PathVariable String userId) {

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = mapper.map(orderDetails, OrderDto.class);
        orderDto.setUserId(userId);
        OrderDto createdOrder = orderService.createOrder(orderDto);

        ResponseOrder responseOrder = mapper.map(createdOrder, ResponseOrder.class);

        return ResponseEntity.status(CREATED).body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable String userId) {
        Iterable<OrderEntity> orderList = orderService.getOrdersByUserId(userId);

        ArrayList<ResponseOrder> result = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        orderList.forEach(orderEntity ->
                result.add(mapper.map(orderEntity, ResponseOrder.class)));

        return ResponseEntity.status(200).body(result);
    }


    @GetMapping("/health_check")
    public String status(HttpServletRequest request) {
        log.debug("health_check health_check");

        String traceId = MDC.get("traceId");
        log.info(traceId);

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            log.info(headerName + ": " + request.getHeader(headerName));
        }

        return String.format("It's Working in User Service on PORT %s",
                env.getProperty("local.server.port"));
    }
}
