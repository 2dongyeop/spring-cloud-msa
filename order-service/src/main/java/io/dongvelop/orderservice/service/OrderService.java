package io.dongvelop.orderservice.service;

import io.dongvelop.orderservice.dto.OrderDto;
import io.dongvelop.orderservice.jpa.OrderEntity;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 27
 * @description
 */
public interface OrderService {
    OrderDto createOrder(OrderDto orderDetails);
    OrderDto getOrderByOrderId(String orderId);
    Iterable<OrderEntity> getOrdersByUserId(String userId);
}
