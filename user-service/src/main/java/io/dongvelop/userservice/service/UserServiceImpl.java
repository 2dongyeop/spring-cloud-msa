package io.dongvelop.userservice.service;

import io.dongvelop.userservice.dto.UserDto;
import io.dongvelop.userservice.repository.UserEntity;
import io.dongvelop.userservice.repository.UserRepository;
import io.dongvelop.userservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 22
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    @Value("${url.order-service.order}")
    private String orderUrl;

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

    @Override
    public UserDto getUserById(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        log.info("orderUrl[{}]", orderUrl);
        String formatted = String.format(orderUrl, userId);
        log.info("formatted[{}]", formatted);

        /* 1. Using RestTemplate */
        final ResponseEntity<List<ResponseOrder>> orderListResponse = restTemplate.exchange(
                formatted,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        final List<ResponseOrder> orderList = orderListResponse.getBody();

        userDto.setOrders(orderList);
        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {

        final UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException("not found");
        }

        return new ModelMapper().map(userEntity, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(username, userEntity.getEncryptedPwd(), true, true, true, true, new ArrayList<>());
    }
}
