package io.dongvelop.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 10. 29
 * @description
 */
@Data
@AllArgsConstructor
public class Field {

    private String type;
    private boolean optional;
    private String field;
}
