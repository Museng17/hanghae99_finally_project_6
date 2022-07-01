package com.hanghae99.finalproject.jwt;

import com.hanghae99.finalproject.model.entity.Users;
import com.hanghae99.finalproject.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static com.hanghae99.finalproject.jwt.JwtTokenProvider.CLAIMS_KEY;

@RequiredArgsConstructor
@Component
public class UserInfoInJwt {

    private final JwtTokenProvider jwtTokenProvider;

    public String getEmail_InJWT(String authorization) {
        String accessToken = authorization.substring(7);
        Claims accessClaims = jwtTokenProvider.getClaimsFormToken(accessToken);
        return (String) accessClaims.get(CLAIMS_KEY);
    }
}
