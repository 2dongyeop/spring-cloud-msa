package io.dongvelop.orderservice.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 10. 29
 * @description
 */
@Data
@Builder
public class Payload {
    private String order_id;
    private String user_id;
    private String product_id;
    private int qty;
    private int unit_price;
    private int total_price;
}
