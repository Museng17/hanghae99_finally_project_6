package com.hanghae99.finalproject.interceptor;

import com.hanghae99.finalproject.jwt.*;
import com.hanghae99.finalproject.util.exceptionHandler.CustumException.NotTokenHeaderException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.*;

import java.util.Optional;

import static com.hanghae99.finalproject.jwt.JwtTokenProvider.*;

@Component
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {

    public final static String JWT_HEADER_KEY = "Authorization";
    public final static String BEARER = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final UserInfoInJwt userInfoInJwt;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws NotTokenHeaderException {

        String authorization = request.getHeader(JWT_HEADER_KEY);

        if (authorization == null) {
            throw new NotTokenHeaderException("헤더에 토큰이 없습니다.");
        }

        Claims decodeToken = userInfoInJwt.getRefreshToken(authorization);

        if (Optional.ofNullable((String) decodeToken.get(REFRESH_TOKEN)).isPresent()) {
            throw new IllegalArgumentException("AccessToken이 아닙니다.");
        }

        jwtTokenProvider.validToken(authorization);

        request.setAttribute(JWT_HEADER_KEY, decodeToken.get(CLAIMS_KEY).toString());
        return true;
    }
}
