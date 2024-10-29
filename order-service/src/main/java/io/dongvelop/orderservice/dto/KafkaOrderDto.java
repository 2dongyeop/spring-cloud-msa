package io.dongvelop.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 10. 29
 * @description
 */
@Data
@AllArgsConstructor
public class KafkaOrderDto implements Serializable {

    private Schema schema;
    private Payload payload;
}
