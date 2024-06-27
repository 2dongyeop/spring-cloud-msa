package io.dongvelop.orderservice.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 27
 * @description
 */
@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Long> {

    Optional<OrderEntity> findById(Long orderId);
    Iterable<OrderEntity> findByUserId(String userId);
}
