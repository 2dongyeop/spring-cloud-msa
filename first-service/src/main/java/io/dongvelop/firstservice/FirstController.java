package io.dongvelop.firstservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 13
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/first-service")
@RequiredArgsConstructor
public class FirstController {

    private final Environment env;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the First service.";
    }

    @GetMapping("/message")
    public String welcome(@RequestHeader("first-request") final String header) {
        log.info("header[{}]", header);
        return "Welcome to the First service.";
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request) {
        log.info("Server port : {}", request.getServerPort());
        return String.format("Hi, there. This is a message from first service on PORT %s",
                env.getProperty("local.server.port"));
    }
}
