package io.dongvelop.catalogservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 27
 * @description
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCatalog {

    private String productId;
    private String productName;
    private Integer unitPrice;
    private Integer stock;
    private Date createdAt;
}
