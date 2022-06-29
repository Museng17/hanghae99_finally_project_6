package com.hanghae99.finalproject.interceptor;

import com.hanghae99.finalproject.jwt.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.*;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {

    public final static String JWT_HEADER_KEY = "Authorization";
    public final static String BEARER = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final UserInfoInJwt userInfoInJwt;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        String authorization = request.getHeader(JWT_HEADER_KEY);
        String accessToken = authorization.substring(7);

        if (authorization == null) {
            throw new RuntimeException("헤더에 토큰을 담지 안았습니다.");
        }

        if (!authorization.startsWith(BEARER)) {
            throw new RuntimeException("Bearer 를 붙혀주세요.");
        }

        if (!jwtTokenProvider.isValidAccessToken(accessToken)) {
            throw new RuntimeException("유효하지 않은 않은 토큰입니다.");
        }

        request.setAttribute(JWT_HEADER_KEY, userInfoInJwt.getEmail_InJWT(authorization));
        return true;
    }
}
