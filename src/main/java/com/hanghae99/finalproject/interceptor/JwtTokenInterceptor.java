package com.hanghae99.finalproject.interceptor;

import com.hanghae99.finalproject.jwt.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.*;

@Component
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {

    public final static String JWT_HEADER_KEY = "Authorization";
    public final static String BEARER = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final UserInfoInJwt userInfoInJwt;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String authorization = request.getHeader(JWT_HEADER_KEY);

        if (authorization == null) {
            throw new RuntimeException("헤더에 토큰을 담지 안았습니다.");
        }

        jwtTokenProvider.validToken(authorization);

        request.setAttribute(JWT_HEADER_KEY, userInfoInJwt.getEmail_InJWT(authorization));
        return true;
    }
}
