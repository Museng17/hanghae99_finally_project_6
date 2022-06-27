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

    private final JwtTokenProvider jwtTokenProvider;
    private final UserInfoInJwt userInfoInJwt;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        String authorization = request.getHeader("Authorization");
        String accessToken = authorization.substring(7);

        if(authorization == null){
            throw new RuntimeException("유효하지 않은 않은 토큰입니다.");
        }

        if (!authorization.startsWith("Bearer ")) {
            throw new RuntimeException("유효하지 않은 않은 토큰입니다.");
        }


        if (!jwtTokenProvider.isValidAccessToken(accessToken)) {
            throw new RuntimeException("유효하지 않은 않은 토큰입니다.");
        }

        request.setAttribute("Authorization", userInfoInJwt.getEmail_InJWT(authorization));
        return true;
    }
}
