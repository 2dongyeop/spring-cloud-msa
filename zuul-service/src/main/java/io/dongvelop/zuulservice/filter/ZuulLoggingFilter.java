package io.dongvelop.zuulservice.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 18
 * @description
 */
@Slf4j
@Component
public class ZuulLoggingFilter extends ZuulFilter {

    /**
     * 실제 동작
     */
    @Override
    public Object run() {
        log.info("*********** printing logs: ");

        final RequestContext context = RequestContext.getCurrentContext();
        final HttpServletRequest request = context.getRequest();

        log.info("*********** {}", request.getRequestURI());
        return null;
    }

    /**
     * 사전 필터임을 의미
     */
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    /**
     * 필터로 사용함을 명시
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }
}
