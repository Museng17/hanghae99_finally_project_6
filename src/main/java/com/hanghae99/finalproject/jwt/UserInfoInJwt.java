package com.hanghae99.finalproject.jwt;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.hanghae99.finalproject.jwt.JwtTokenProvider.*;

@RequiredArgsConstructor
@Component
public class UserInfoInJwt {

    private final JwtTokenProvider jwtTokenProvider;

    public String getEmail_InJWT(String authorization) {
        String accessToken = authorization.substring(7);
        Claims accessClaims = jwtTokenProvider.getClaimsFormToken(accessToken);
        return (String) accessClaims.get(CLAIMS_KEY);
    }

    public Claims getRefreshToken(String authorization) {
        String accessToken = authorization.substring(7);
        return jwtTokenProvider.getClaimsFormToken(accessToken);
    }
}
