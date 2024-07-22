package io.dongvelop.userservice.controller;

import io.dongvelop.userservice.dto.UserDto;
import io.dongvelop.userservice.repository.UserEntity;
import io.dongvelop.userservice.service.UserService;
import io.dongvelop.userservice.vo.Greeting;
import io.dongvelop.userservice.vo.RequestUser;
import io.dongvelop.userservice.vo.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final Environment env;
    private final Greeting greeting;
    private final ModelMapper mapper;
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers() {
        Iterable<UserEntity> userList = userService.getUserByAll();

        List<ResponseUser> result = new ArrayList<>();
        userList.forEach(userEntity -> result.add(mapper.map(userEntity, ResponseUser.class)));

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUsers(@PathVariable final String userId) {
        UserDto userDto = userService.getUserById(userId);
        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody final RequestUser user) {

        // RequestUser -> UserDto
        final UserDto userDto = mapper.map(user, UserDto.class);
        userService.createUser(userDto);

        // UserDto -> ResponseUser
        final ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in User Service on PORT %s",
                env.getProperty("local.server.port")) + "token.expiration_time =" + env.getProperty("token.expiration_time");
    }

    @GetMapping("/welcome")
    public String welcome() {
        return greeting.getMessage();
    }
}
