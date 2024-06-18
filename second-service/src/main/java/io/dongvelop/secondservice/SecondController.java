package io.dongvelop.secondservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 13
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/second-service")
public class SecondController {

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the First service.";
    }

    @GetMapping("/message")
    public String welcome(@RequestHeader("second-request") final String header) {
        log.info("header[{}]", header);
        return "Welcome to the Second service.";
    }

    @GetMapping("/check")
    public String check() {
        return "Hi, there. This is a message from second service";
    }
}
