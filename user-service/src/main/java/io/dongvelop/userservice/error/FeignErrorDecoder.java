package io.dongvelop.userservice.error;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 10. 14
 * @description
 */
@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Value("${url.order-service.exception.order_is_empty}")
    private String orderEmptyMessage;

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400 -> {
            }
            case 404 -> {
                if (methodKey.contains("getOrders")) {
                    System.out.println("methodKey: " + methodKey);
                    System.out.println("orderEmptyMessage = " + orderEmptyMessage);
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()), orderEmptyMessage);
                }
            }
            default -> {
                return new Exception(response.reason());
            }
        }

        return null;
    }
}
