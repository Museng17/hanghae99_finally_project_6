package com.hanghae99.finalproject.util.restTemplates;

import com.hanghae99.finalproject.model.dto.SocialLoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class SocialLoginRestTemplate {

    private final RestTemplate restTemplate;
    private final static String GOOGLE_LOGIN_BASE_URL = "https://oauth2.googleapis.com/tokeninfo?id_token=";

    public ResponseEntity<SocialLoginRequestDto> googleLogin(String credential) {
        ResponseEntity<SocialLoginRequestDto> responseEntity;
        try {
            responseEntity = restTemplate.exchange(
                    GOOGLE_LOGIN_BASE_URL + credential,
                    HttpMethod.GET,
                    new HttpEntity<String>(new HttpHeaders()),
                    SocialLoginRequestDto.class
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return responseEntity;
    }
}
