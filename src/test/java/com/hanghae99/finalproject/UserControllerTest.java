package com.hanghae99.finalproject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99.finalproject.jwt.UserInfoInJwt;
import com.hanghae99.finalproject.model.entity.Users;
import com.hanghae99.finalproject.model.repository.*;
import lombok.*;
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
@DisplayName("UserController 클래스")
public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserInfoInJwt userInfoInJwt;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolderRepository folderRepository;

    private HttpHeaders headers;
    private ObjectMapper mapper = new ObjectMapper();

    private TokenDto token;

    @BeforeAll
    public void setup() throws JsonProcessingException {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UserDto userDto = UserDto.builder()
                .email("whitew295@gmail.com")
                .username("test4321")
                .password("testest1234")
                .nickname("테스트코드유저")
                .build();

        String requestBody = mapper.writeValueAsString(userDto);
        HttpEntity<String> stringHttpEntity = new HttpEntity<>(requestBody, headers);

        //when
        ResponseEntity<UserRegisterRespDto> userDtoResponseEntity = restTemplate.postForEntity(
                "/user/signup?isEmailCheck=false",
                stringHttpEntity,
                UserRegisterRespDto.class
        );

        //then
        assertEquals(HttpStatus.OK, userDtoResponseEntity.getStatusCode());

        UserRegisterRespDto responseUserDto = userDtoResponseEntity.getBody();
        assertEquals(200, responseUserDto.getStatusCode());
        assertEquals("회원가입 성공", responseUserDto.getErrorMsg());

    }

    @AfterAll
    @Rollback(value = false)
    public void deleteTestUser() {
        Users users = userRepository.findByUsername("test4321")
                .orElseThrow(() -> new RuntimeException("TestCode 회원을 못찾았습니다."));
        folderRepository.deleteByUsersId(users.getId());
        userRepository.deleteById(users.getId());
    }

    @Nested
    @Order(1)
    class 회원가입_부터_로그인기능 {

        @Nested
        @Order(1)
        class 회원가입_중복체크 {

            @Test
            @DisplayName("아이디 중복체크")
            public void 아이디중복체크() {

                //when
                ResponseEntity<Boolean> 실패 = restTemplate.getForEntity(
                        "/user/emailDupCheck/test4321",
                        Boolean.class
                );

                //then
                assertEquals(HttpStatus.OK, 실패.getStatusCode());
                assertEquals(false, 실패.getBody());

                //when
                ResponseEntity<Boolean> 성공 = restTemplate.getForEntity(
                        "/user/emailDupCheck/없을껄?",
                        Boolean.class
                );

                //then
                assertEquals(HttpStatus.OK, 실패.getStatusCode());
                assertEquals(true, 성공.getBody());
            }

            @Test
            @DisplayName("닉네임중복체크")
            public void 닉네임중복체크() {
                //when
                ResponseEntity<Boolean> 실패 = restTemplate.getForEntity(
                        "/user/nameDupCheck/테스트코드유저",
                        Boolean.class
                );

                //then
                assertEquals(HttpStatus.OK, 실패.getStatusCode());
                assertEquals(false, 실패.getBody());

                //when
                ResponseEntity<Boolean> 성공 = restTemplate.getForEntity(
                        "/user/nameDupCheck/테스트코드유저2",
                        Boolean.class
                );

                //then
                assertEquals(HttpStatus.OK, 실패.getStatusCode());
                assertEquals(true, 성공.getBody());
            }
        }

        @Nested
        @Order(2)
        class 로그인_실패와_성공_테스트_및_리프레쉬토큰_사용 {

            @Test
            @DisplayName("로그인 성공")
            public void itSuccessLogin() throws JsonProcessingException {
                UserDto userDto = UserDto.builder()
                        .username("test4321")
                        .password("testest1234")
                        .build();

                String body = mapper.writeValueAsString(userDto);
                HttpEntity<String> entity = new HttpEntity<>(body, headers);

                //then
                ResponseEntity<TokenDto> responseEntity2 = restTemplate.postForEntity(
                        "/user/login",
                        entity,
                        TokenDto.class
                );

                //when
                assertEquals(HttpStatus.OK, responseEntity2.getStatusCode());
                TokenDto tokenDto = responseEntity2.getBody();

                //토큰을 파싱했을 때 로그인한 username이 같은지
                assertEquals(
                        userDto.getUsername(),
                        userInfoInJwt.getToken("Bearer " + tokenDto.getAccessToken()).get("username")
                );
            }

            @Test
            @DisplayName("아이디 틀려서 로그인 실패")
            public void itFailNotUsername() throws JsonProcessingException {
                UserDto failUsername = UserDto.builder()
                        .username("테스트코드유저")
                        .password("testest1234")
                        .build();

                String body = mapper.writeValueAsString(failUsername);
                HttpEntity<String> entity = new HttpEntity<>(body, headers);

                //then
                ResponseEntity<RuntimeException> responseEntity = restTemplate.postForEntity(
                        "/user/login",
                        entity,
                        RuntimeException.class
                );

                //when
                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
            }

            @Test
            @DisplayName("비밀번호 틀려서 로그인 실패")
            public void itFailNotPassword() throws JsonProcessingException {
                UserDto failUsername = UserDto.builder()
                        .username("test4321")
                        .password("testest12345")
                        .build();

                String body = mapper.writeValueAsString(failUsername);
                HttpEntity<String> entity = new HttpEntity<>(body, headers);

                //then
                ResponseEntity<RuntimeException> responseEntity = restTemplate.postForEntity(
                        "/user/login",
                        entity,
                        RuntimeException.class
                );

                //when
                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

            }

            @Test
            @DisplayName("토큰재발급")
            public void refreshToken() throws JsonProcessingException {
                UserDto userDto = UserDto.builder()
                        .username("test4321")
                        .password("testest1234")
                        .build();

                String body = mapper.writeValueAsString(userDto);
                HttpEntity<String> entity = new HttpEntity<>(body, headers);

                //then
                ResponseEntity<TokenDto> responseEntity2 = restTemplate.postForEntity(
                        "/user/login",
                        entity,
                        TokenDto.class
                );

                //when
                assertEquals(HttpStatus.OK, responseEntity2.getStatusCode());
                TokenDto tokenDto = responseEntity2.getBody();

                token = TokenDto.builder()
                        .accessToken("Bearer " + tokenDto.getAccessToken())
                        .refreshToken("Bearer " + tokenDto.getRefreshToken())
                        .build();

                headers.add("RefreshToken", token.getRefreshToken());
                headers.add("Authorization", token.getAccessToken());

                entity = new HttpEntity<>(headers);

                //then
                ResponseEntity<TokenDto> responseEntity = restTemplate.postForEntity(
                        "/user/refresh",
                        entity,
                        TokenDto.class
                );

                //when
                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

                tokenDto = responseEntity.getBody();

                //토큰을 파싱했을 때 로그인한 username이 같은지
                assertEquals(
                        userDto.getUsername(),
                        userInfoInJwt.getToken("Bearer " + tokenDto.getAccessToken()).get("username")
                );
            }
        }
    }

    @Nested
    @Order(2)
    class 토큰사용 {

        @Test
        @DisplayName("유저찾기")
        public void findUsername() {

        }
    }

}

@Getter
@NoArgsConstructor
class UserDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String nickname;

    @Builder
    public UserDto(String username, String password, String nickname, String email) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
    }
}

@Getter
@NoArgsConstructor
class TokenDto {

    private String accessToken;
    private String refreshToken;

    @Builder
    public TokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}

@Getter
class UserRegisterRespDto {
    private int statusCode;
    private boolean result;
    private String errorMsg;
}
