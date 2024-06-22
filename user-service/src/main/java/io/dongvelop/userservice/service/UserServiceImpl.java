package io.dongvelop.userservice.service;

import io.dongvelop.userservice.dto.UserDto;
import io.dongvelop.userservice.repository.UserEntity;
import io.dongvelop.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 22
 * @description
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(final UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        // UserDto -> UserEntity
        final UserEntity userEntity = mapper.map(userDto, UserEntity.class);

        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));
        userRepository.save(userEntity);

        // UserEntity -> UserDto
        return mapper.map(userEntity, UserDto.class);
    }
}
