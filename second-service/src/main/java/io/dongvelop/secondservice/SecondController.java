package io.dongvelop.secondservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 13
 * @description
 */
@RestController
@RequestMapping("/")
public class SecondController {

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the First service.";
    }
}
