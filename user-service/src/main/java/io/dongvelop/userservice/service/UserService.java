package io.dongvelop.userservice.service;

import io.dongvelop.userservice.dto.UserDto;
import io.dongvelop.userservice.repository.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 22
 * @description
 */
public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);

    UserDto getUserById(String userId);
    Iterable<UserEntity> getUserByAll();
}
