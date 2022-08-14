package com.hanghae99.finalproject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99.finalproject.jwt.UserInfoInJwt;
import com.hanghae99.finalproject.model.entity.Users;
import com.hanghae99.finalproject.model.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class BoardControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserInfoInJwt userInfoInJwt;

    private HttpHeaders headers;
    private ObjectMapper mapper = new ObjectMapper();

    private TokenDto token;

//    @BeforeAll
//    public void setup() throws JsonProcessingException {
//        headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        UserDto userDto = UserDto.builder()
//                .email("whitew295@gmail.com")
//                .username("test4321")
//                .password("testest1234")
//                .nickname("테스트코드유저")
//                .build();
//
//        String requestBody = mapper.writeValueAsString(userDto);
//        HttpEntity<String> stringHttpEntity = new HttpEntity<>(requestBody, headers);
//
//        //when
//        ResponseEntity<UserRegisterRespDto> userDtoResponseEntity = restTemplate.postForEntity(
//                "/user/signup?isEmailCheck=false",
//                stringHttpEntity,
//                UserRegisterRespDto.class
//        );
//
//        //then
//        assertEquals(HttpStatus.OK, userDtoResponseEntity.getStatusCode());
//
//        UserRegisterRespDto responseUserDto = userDtoResponseEntity.getBody();
//        assertEquals(200, responseUserDto.getStatusCode());
//        assertEquals("회원가입 성공", responseUserDto.getErrorMsg());
//
//    }
//
//    @AfterAll
//    @Rollback(value = false)
//    public void deleteTestUser() {
//
//    }

}
