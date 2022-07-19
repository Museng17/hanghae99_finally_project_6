package com.hanghae99.finalproject.jwt;

import com.hanghae99.finalproject.exceptionHandler.CustumException.CustomException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;

import java.util.*;

import static com.hanghae99.finalproject.exceptionHandler.CustumException.ErrorCode.*;
import static com.hanghae99.finalproject.interceptor.JwtTokenInterceptor.BEARER;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("login.secret_key")
    private String SECRET_KEY;

    public final static String CLAIMS_KEY = "username";
    public final static String REFRESH_TOKEN = "RefreshToken";

    private static final Long TokenValidTime = 1000L * 60;  //1분

    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    public String createAccessToken(String username) {
        Claims claims = Jwts.claims();//.setSubject(userPk); // JWT payload 에 저장되는 정보단위
        claims.put(CLAIMS_KEY, username);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + TokenValidTime * 30)) // 30분
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 사용할 암호화 알고리즘과
                .compact();
    }

    public String createAccessToken2(String username) {
        Claims claims = Jwts.claims();//.setSubject(userPk); // JWT payload 에 저장되는 정보단위
        claims.put(CLAIMS_KEY, username);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + TokenValidTime * 1)) // 30분
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 사용할 암호화 알고리즘과
                .compact();
    }

    public String createRefreshToken(String username) {
        Claims usernameClaims = Jwts.claims();
        Claims refreshClaims = Jwts.claims();
        usernameClaims.put(REFRESH_TOKEN, REFRESH_TOKEN);
        usernameClaims.put(CLAIMS_KEY, username);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(usernameClaims)
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + TokenValidTime * 60)) // 60분
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 사용할 암호화 알고리즘과
                .compact();
    }

    public String createRefreshToken2(String username) {
        Claims usernameClaims = Jwts.claims();
        Claims refreshClaims = Jwts.claims();
        usernameClaims.put(REFRESH_TOKEN, REFRESH_TOKEN);
        usernameClaims.put(CLAIMS_KEY, username);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(usernameClaims)
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + TokenValidTime * 2)) // 60분
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 사용할 암호화 알고리즘과
                .compact();
    }

    //AccessToken 유효성 검사
    public boolean isValidAccessToken(String token) {
        System.out.println("isValidToken is : " + token);
        try {
            getClaimsFormToken(token);
        } catch (ExpiredJwtException exception) {
            throw new CustomException(EXPIRATION_ACCESS_TOKEN);
        } catch (JwtException exception) {
            throw new CustomException(NOT_ACCESS_TOKEN);
        }
        return true;
    }

    public boolean isValidRefreshToken(String token) {
        String accessToken = token.substring(7);
        try {
            getClaimsFormToken(accessToken);
        } catch (ExpiredJwtException exception) {
            throw new CustomException(EXPIRATION_REFRESH_TOKEN);
        } catch (JwtException exception) {
            throw new CustomException(NOT_REFRESH_TOKEN);
        }
        return true;
    }

    public void isHeaderToken(String token) {
        if (token.equals("noToken")) {
            throw new CustomException(NOT_HEADER_REFRESH_TOKEN);
        }
    }

    //JWT 구문분석 함수
    public Claims getClaimsFormToken(String token) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isBearerToken(String authorization) {

        String accessToken = authorization.substring(7);

        if (!authorization.startsWith(BEARER)) {
            throw new IllegalArgumentException("Bearer 를 붙혀주세요.");
        }

        if (!isValidAccessToken(accessToken)) {
            throw new IllegalArgumentException("유효하지 않은 않은 토큰입니다.");
        }

        return true;
    }
}
