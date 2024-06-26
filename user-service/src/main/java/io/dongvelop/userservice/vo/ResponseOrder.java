package io.dongvelop.userservice.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 26
 * @description
 */
@Data
public class ResponseOrder {
    private String productId;
    private Integer qty; // quantity
    private Integer unitPrice; // 단가
    private Integer totalPrice;
    private Date createdAt;

    private String orderId;
}
