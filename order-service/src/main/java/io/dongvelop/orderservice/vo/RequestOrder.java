package io.dongvelop.orderservice.vo;

import lombok.Data;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 27
 * @description
 */
@Data
public class RequestOrder {

    private String productId;
    private Integer qty;
    private Integer unitPrice;
}
