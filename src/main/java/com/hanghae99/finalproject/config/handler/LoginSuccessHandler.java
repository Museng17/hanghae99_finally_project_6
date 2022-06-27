package com.hanghae99.finalproject.config.handler;

import com.hanghae99.finalproject.config.auth.PrincipalDetails;
import com.hanghae99.finalproject.config.jwt.JwtTokenUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.*;

import java.util.Optional;

/*
 * 인증이 완료되면 사용자 정보를 가진 Authentication 객체를
 * SecurityContextHolder에 담은 이후 AuthenticationSuccessHandle를 실행한다
 * */
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_TYPE = "BEARER";

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final Authentication authentication) {

        if (!Optional.ofNullable(authentication.getPrincipal()).isPresent()) {
            response.addHeader(AUTH_HEADER, null);
        } else {
            final PrincipalDetails userDetails = ((PrincipalDetails) authentication.getPrincipal());
            // Token 생성
            final String token = JwtTokenUtils.generateJwtToken(userDetails);
            response.addHeader(AUTH_HEADER, TOKEN_TYPE + " " + token);
        }
    }
}
