package io.dongvelop.userservice.service;

import io.dongvelop.userservice.dto.UserDto;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 22
 * @description
 */
public interface UserService {
    UserDto createUser(UserDto userDto);
}
