package io.dongvelop.userservice.controller;

import io.dongvelop.userservice.dto.UserDto;
import io.dongvelop.userservice.service.UserService;
import io.dongvelop.userservice.vo.Greeting;
import io.dongvelop.userservice.vo.RequestUser;
import io.dongvelop.userservice.vo.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final Greeting greeting;
    private final ModelMapper mapper;
    private final UserService userService;

    @GetMapping("/health_check")
    public String status() {
        return "It's Working in User Service";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return greeting.getMessage();
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUSer(@RequestBody final RequestUser user) {

        // RequestUser -> UserDto
        final UserDto userDto = mapper.map(user, UserDto.class);
        userService.createUser(userDto);

        // UserDto -> ResponseUser
        final ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }
}
