package io.dongvelop.userservice.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 30
 * @description
 */
@Data
public class RequestLogin {

    @NotNull
    @Size(min = 2)
    @Email
    private String email;

    @NotNull
    @Size(min = 8)
    private String password;
}
