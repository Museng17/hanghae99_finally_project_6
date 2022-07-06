package com.hanghae99.finalproject.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;

import java.util.*;

import static com.hanghae99.finalproject.interceptor.JwtTokenInterceptor.BEARER;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private String SECRET_KEY = "sec";
    public final static String CLAIMS_KEY = "username";
    public final static String REFRESH_TOKEN = "RefreshToken";

    private static final Long TokenValidTime  = 1000L * 60;  //1분

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
            getClaimsFormToken(token);
            return true;
    }

    //JWT 구문분석 함수
    public Claims getClaimsFormToken(String token) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validToken(String authorization){

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
