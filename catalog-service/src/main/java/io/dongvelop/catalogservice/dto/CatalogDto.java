package io.dongvelop.catalogservice.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 27
 * @description
 */
@Data
public class CatalogDto implements Serializable {

    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;

    private String orderId;
    private String userId;
}
