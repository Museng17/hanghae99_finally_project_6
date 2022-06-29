package com.hanghae99.finalproject.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;

import java.util.*;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private String SECRET_KEY = "sec";
    public final static String CLAIMS_KEY = "username";

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

    public String createRefreshToken(String username) {
        Claims claims = Jwts.claims();//.setSubject(userPk); // JWT payload 에 저장되는 정보단위
        claims.put(CLAIMS_KEY, username);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + TokenValidTime * 60)) // 60분
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 사용할 암호화 알고리즘과
                .compact();
    }

    //AccessToken 유효성 검사
    public boolean isValidAccessToken(String token) {
        try {
            getClaimsFormToken(token);
            return true;
        } catch (ExpiredJwtException exception) {
            throw new RuntimeException("만료일 " + exception.getClaims().getExpiration() + " : 만료되었습니다.");
        } catch (JwtException exception) {
            throw new RuntimeException("변조된 토큰입니다.");
        }
    }

    //JWT 구문분석 함수
    public Claims getClaimsFormToken(String token) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(token)
                .getBody();
    }
}
