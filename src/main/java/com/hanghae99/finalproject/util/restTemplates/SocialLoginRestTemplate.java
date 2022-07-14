package com.hanghae99.finalproject.util.restTemplates;

import com.hanghae99.finalproject.model.dto.requestDto.SocialLoginRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.*;

import static com.hanghae99.finalproject.util.resultType.GoogleLoginType.*;

@Component
public class SocialLoginRestTemplate {

    private final RestTemplate restTemplate;
    private final String clientId;
    private final String clientSecret;

    public SocialLoginRestTemplate(RestTemplate restTemplate,
                                   @Value("${client_id}") String clientId,
                                   @Value("${secret_key}") String clientSecret) {
        this.restTemplate = restTemplate;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public SocialLoginRequestDto findAccessTokenByCode(String code) {
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<SocialLoginRequestDto> responseEntity;
        try {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            UriComponents googleUrl = UriComponentsBuilder.newInstance()
                    .scheme(PROTOCOL.getValue())
                    .host(GOOGLE_ACCESS_TOKEN_URL.getValue())
                    .path(GOOGLE_ACCESS_PATH.getValue())
                    .build(true);

            UriComponents body = UriComponentsBuilder.newInstance()
                    .queryParam(CODE.getName(), code)
                    .queryParam(CLIENT_ID.getName(), clientId)
                    .queryParam(CLIENT_SECRET.getName(), clientSecret)
                    .queryParam(GRANT_TYPE.getName(), GRANT_TYPE.getValue())
                    .queryParam(REDIRECT_URI.getName(), REDIRECT_URI.getValue())
                    .build(true);

            responseEntity = restTemplate.exchange(
                    googleUrl.toString(),
                    HttpMethod.POST,
                    new HttpEntity<String>(body.toString().substring(1), headers),
                    SocialLoginRequestDto.class
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return statusCheck(responseEntity);
    }

    public SocialLoginRequestDto googleUserInfoByAccessToken(String accessToken) {
        ResponseEntity<SocialLoginRequestDto> responseEntity;
        try {
            UriComponents googleUrl = UriComponentsBuilder.newInstance()
                    .scheme(PROTOCOL.getValue())
                    .host(GOOGLE_USER_INFO_URL.getValue())
                    .path(GOOGLE_USER_INFO_PATH.getValue())
                    .queryParam(ACCESS_TOKEN.getName(), accessToken)
                    .build(true);

            responseEntity = restTemplate.exchange(
                    googleUrl.toString(),
                    HttpMethod.GET,
                    new HttpEntity<String>("", new HttpHeaders()),
                    SocialLoginRequestDto.class
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return statusCheck(responseEntity);
    }

    private SocialLoginRequestDto statusCheck(ResponseEntity<SocialLoginRequestDto> socialLoginRequestDtoResponseEntity){
        int statusCode = socialLoginRequestDtoResponseEntity.getStatusCode().value();

        if (statusCode != 200) {
            throw new RuntimeException("statusCode = " + statusCode);
        }
        return socialLoginRequestDtoResponseEntity.getBody();
    }

}
