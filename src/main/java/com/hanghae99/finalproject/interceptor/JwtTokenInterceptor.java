package com.hanghae99.finalproject.interceptor;

import com.hanghae99.finalproject.exceptionHandler.CustumException.CustomException;
import com.hanghae99.finalproject.jwt.*;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.*;

import java.util.Optional;

import static com.hanghae99.finalproject.exceptionHandler.CustumException.ErrorCode.*;
import static com.hanghae99.finalproject.jwt.JwtTokenProvider.*;

@Component
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {

    public final static String JWT_HEADER_KEY = "Authorization";
    public final static String BEARER = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final UserInfoInJwt userInfoInJwt;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws CustomException {

        String authorization = request.getHeader(JWT_HEADER_KEY);

        if (authorization == null) {
            throw new CustomException(NOT_HEADER_ACCESS_TOKEN);
        }

        jwtTokenProvider.isBearerToken(authorization);

        Claims decodeToken = userInfoInJwt.getToken(authorization);

        if (Optional.ofNullable((String) decodeToken.get(REFRESH_TOKEN)).isPresent()) {
            throw new CustomException(NOT_ACCESS_TOKEN);
        }

        request.setAttribute(JWT_HEADER_KEY, decodeToken.get(CLAIMS_KEY).toString());
        return true;
    }
}
