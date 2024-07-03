package io.dongvelop.userservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dongvelop.userservice.dto.UserDto;
import io.dongvelop.userservice.service.UserService;
import io.dongvelop.userservice.vo.RequestLogin;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 30
 * @description
 */
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserService userService;
    private final Environment environment;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            // Login 요청이 POST 형식이므로, 파라미터로 받을 수가 없음.
            // 따라서 InputStream 으로 받은 후, 수작업으로 매핑
            final RequestLogin credential = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);
            final ArrayList<GrantedAuthority> authorities = new ArrayList<>();

            final var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    credential.getEmail(),
                    credential.getPassword(),
                    authorities
            );

            return getAuthenticationManager().authenticate(
                    usernamePasswordAuthenticationToken
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        // TODO : 로그인 성공시, 어떤 처리를 할 것인지 (ex. 토큰 발급, 만료시간 관리 등..) 처리 예정
//        super.successfulAuthentication(request, response, chain, authResult);

        final User user = (User) authResult.getPrincipal();
        final String username = user.getUsername();
        log.info("username = {}", username);

        final UserDto userDetails = userService.getUserDetailsByEmail(username);

        String token = Jwts.builder()
                .setSubject(userDetails.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(Objects.requireNonNull(environment.getProperty("token.expiration_time")))))
                .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
                .compact();
        log.info("token[{}]", token);

        response.addHeader("token", token);
        response.addHeader("userId", userDetails.getUserId());
    }
}
