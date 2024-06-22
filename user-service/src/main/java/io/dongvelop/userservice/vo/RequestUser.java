package io.dongvelop.userservice.vo;

import lombok.Data;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 22
 * @description
 */
@Data
public class RequestUser {
    private String email;
    private String name;
    private String pwd;
}
