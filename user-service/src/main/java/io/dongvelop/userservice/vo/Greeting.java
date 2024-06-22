package io.dongvelop.userservice.vo;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 22
 * @description
 */
@Getter
@Component
public class Greeting {

    @Value("${greeting.message}")
    private String message;
}
