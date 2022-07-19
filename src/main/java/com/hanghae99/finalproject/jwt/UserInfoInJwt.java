package com.hanghae99.finalproject.jwt;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserInfoInJwt {

    private final JwtTokenProvider jwtTokenProvider;

    public Claims getToken(String authorization) {
        String accessToken = authorization.substring(7);
        return jwtTokenProvider.getClaimsFormToken(accessToken);
    }
}
