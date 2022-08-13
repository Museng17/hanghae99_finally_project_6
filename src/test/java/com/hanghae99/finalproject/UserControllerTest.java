package com.hanghae99.finalproject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99.finalproject.jwt.UserInfoInJwt;
import com.hanghae99.finalproject.model.entity.Users;
import com.hanghae99.finalproject.model.repository.*;
import com.hanghae99.finalproject.model.resultType.LoginType;
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

                token = TokenDto.builder()
                        .accessToken("Bearer " + tokenDto.getAccessToken())
                        .refreshToken("Bearer " + tokenDto.getRefreshToken())
                        .build();

                headers.add("RefreshToken", token.getRefreshToken());
                headers.add("Authorization", token.getAccessToken());

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

                HttpEntity<Object> entity = new HttpEntity<>(headers);

                //then
                ResponseEntity<TokenDto> responseEntity = restTemplate.postForEntity(
                        "/user/refresh",
                        entity,
                        TokenDto.class
                );

                //when
                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

                TokenDto tokenDto = responseEntity.getBody();

                //토큰을 파싱했을 때 로그인한 username이 같은지
                assertEquals(
                        userDto.getUsername(),
                        userInfoInJwt.getToken("Bearer " + tokenDto.getAccessToken()).get("username")
                );
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Order(2)
    class 토큰사용 {

        @Test
        @Order(1)
        @DisplayName("닉네임변경")
        public void updateUserNickname() throws JsonProcessingException {
            UserDto userDto = UserDto.builder()
                    .nickname("테스트유저닉네임변경")
                    .build();

            HttpEntity request = new HttpEntity(mapper.writeValueAsString(userDto), headers);

            ResponseEntity<MessageDto> entity = restTemplate.exchange(
                    "/user/updateName",
                    HttpMethod.PUT,
                    request,
                    MessageDto.class
            );

            assertEquals(HttpStatus.OK, entity.getStatusCode());

            MessageDto messageDto = entity.getBody();

            assertEquals("계정 닉네임 변경 성공", messageDto.getMessage());
            assertEquals(200, messageDto.getStatusCode());
        }

        @Test
        @Order(2)
        @DisplayName("유저 정보 변경")
        public void userUpdateInfo() throws JsonProcessingException {
            UserDto userDto = UserDto.builder()
                    .information("추가된 인포메이션")
                    .build();

            HttpEntity request = new HttpEntity(mapper.writeValueAsString(userDto), headers);

            ResponseEntity<MessageDto> entity = restTemplate.exchange(
                    "/user/updateInfo",
                    HttpMethod.PUT,
                    request,
                    MessageDto.class
            );

            assertEquals(HttpStatus.OK, entity.getStatusCode());

            MessageDto messageDto = entity.getBody();

            assertEquals("계정 정보 수정 완료", messageDto.getMessage());
            assertEquals(200, messageDto.getStatusCode());
        }

        @Test
        @Order(3)
        @DisplayName("유저 비밀번호 확인")
        public void userPwCheck() throws JsonProcessingException {
            UserDto userDto = UserDto.builder()
                    .password("testest1234")
                    .build();

            HttpEntity request = new HttpEntity(mapper.writeValueAsString(userDto), headers);

            ResponseEntity<MessageDto> entity = restTemplate.exchange(
                    "/user/pw/check",
                    HttpMethod.POST,
                    request,
                    MessageDto.class
            );

            assertEquals(HttpStatus.OK, entity.getStatusCode());

            MessageDto messageDto = entity.getBody();

            assertEquals("비밀번호 확인 성공", messageDto.getMessage());
            assertEquals(200, messageDto.getStatusCode());
        }

        @Test
        @Order(4)
        @DisplayName("유저 비밀번호 변경")
        public void userPwUpdate() throws JsonProcessingException {
            UserDto userDto = UserDto.builder()
                    .password("testest1234")
                    .newPassword("testest4321")
                    .build();

            HttpEntity request = new HttpEntity(mapper.writeValueAsString(userDto), headers);

            ResponseEntity<MessageDto> entity = restTemplate.exchange(
                    "/user/pw/update",
                    HttpMethod.PUT,
                    request,
                    MessageDto.class
            );

            assertEquals(HttpStatus.OK, entity.getStatusCode());

            MessageDto messageDto = entity.getBody();

            assertEquals("계정 비밀번호 변경 완료", messageDto.getMessage());
            assertEquals(200, messageDto.getStatusCode());
        }

        @Test
        @Order(5)
        @DisplayName("유저 비밀번호 확인 틀렸을 경우")
        public void userPwCheckFail() throws JsonProcessingException {
            UserDto userDto = UserDto.builder()
                    .password("testest1234")
                    .build();

            HttpEntity request = new HttpEntity(mapper.writeValueAsString(userDto), headers);

            ResponseEntity<MessageDto> entity = restTemplate.exchange(
                    "/user/pw/check",
                    HttpMethod.POST,
                    request,
                    MessageDto.class
            );

            assertEquals(HttpStatus.OK, entity.getStatusCode());

            MessageDto messageDto = entity.getBody();

            assertEquals("비밀번호가 다릅니다", messageDto.getMessage());
            assertEquals(501, messageDto.getStatusCode());
        }

        @Test
        @Order(6)
        @DisplayName("유저 프로필 보기")
        public void findUserProfile() {
            UserDto userDto = UserDto.builder()
                    .email("whitew295@gmail.com")
                    .username("test4321")
                    .password("testest1234")
                    .information("추가된 인포메이션")
                    .nickname("테스트유저닉네임변경")
                    .build();

            HttpEntity<Object> entity = new HttpEntity<>(headers);

            //then
            ResponseEntity<UserDto> responseEntity = restTemplate.exchange(
                    "/user/profile",
                    HttpMethod.GET,
                    entity,
                    UserDto.class
            );

            //when
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

            UserDto entityBody = responseEntity.getBody();

            assertEquals(userDto.getUsername(), entityBody.getUsername());
            assertEquals(userDto.getEmail(), entityBody.getEmail());
            assertEquals(userDto.getInformation(), entityBody.getInformation());
            assertEquals(0L, entityBody.getFolderCnt());
            assertEquals(0L, entityBody.getBoardCnt());
            assertEquals(0L, entityBody.getReportCnt());
            assertEquals(LoginType.USER, entityBody.getLoginType());

        }
    }

}

@Getter
@NoArgsConstructor
class UserDto {
    private Long id;
    private String username;
    private String nickname;
    private String imgPath;
    private String information;
    private String password;
    private String email;
    private Long folderCnt;
    private String newPassword;
    private Long boardCnt;
    private Long reportCnt = 0L;
    private LoginType loginType;

    @Builder
    public UserDto(Long id,
                   String username,
                   String nickname,
                   String imgPath,
                   String information,
                   String password,
                   String email,
                   Long folderCnt,
                   Long boardCnt, Long reportCnt,
                   LoginType loginType,
                   String newPassword) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.imgPath = imgPath;
        this.information = information;
        this.password = password;
        this.email = email;
        this.folderCnt = folderCnt;
        this.boardCnt = boardCnt;
        this.reportCnt = reportCnt;
        this.loginType = loginType;
        this.newPassword = newPassword;
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

@Getter
@NoArgsConstructor
class MessageDto<T> {
    private int statusCode;
    private String message;
    private int totalPages;
    private T content;
}