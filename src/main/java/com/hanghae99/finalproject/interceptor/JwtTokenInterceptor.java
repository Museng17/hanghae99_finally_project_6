package com.hanghae99.finalproject.interceptor;

import com.hanghae99.finalproject.exceptionHandler.CustumException.CustomException;
import com.hanghae99.finalproject.jwt.*;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.*;

import java.util.*;

import static com.hanghae99.finalproject.exceptionHandler.CustumException.ErrorCode.*;
import static com.hanghae99.finalproject.jwt.JwtTokenProvider.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {

    public final static String JWT_HEADER_KEY = "Authorization";
    public final static String BEARER = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final UserInfoInJwt userInfoInJwt;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws CustomException {
        logSave(request);

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

    private void logSave(HttpServletRequest request){
        log.info("요청한 Method : "+request.getMethod());
        log.info("요청한 URL : "+request.getRequestURI());

        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()){
            String thisHeader = headers.nextElement();
            log.info("요청한 request Header 키값 "+thisHeader);
        }
    }
}
