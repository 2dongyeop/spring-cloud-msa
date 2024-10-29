package io.dongvelop.orderservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 10. 29
 * @description
 */
@Data
@Builder
public class Schema {

    private String type;
    private List<Field> fields;
    private boolean optional;
    private String name;
}
