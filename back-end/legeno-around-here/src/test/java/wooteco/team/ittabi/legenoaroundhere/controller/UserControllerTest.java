package wooteco.team.ittabi.legenoaroundhere.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AUTH_NUMBER;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import wooteco.team.ittabi.legenoaroundhere.dto.LoginRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserCheckRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.AlreadyExistUserException;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;
import wooteco.team.ittabi.legenoaroundhere.service.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .build();
    }

    @Test
    @DisplayName("가입 여부 확인 - 가입되지 않은 메일일 경우")
    void checkJoined() throws Exception {
        String inputJson = objectMapper.writeValueAsString(
            new UserCheckRequest(TEST_USER_EMAIL));

        this.mockMvc.perform(get("/check-joined")
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(print());
    }

    @Test
    @DisplayName("가입 여부 확인 - 이미 가입된 메일일 경우")
    void checkJoined_AlreadyExistUser_ThrowException() throws Exception {
        willThrow(new AlreadyExistUserException("")).given(userService).checkJoined(any());

        String inputJson = objectMapper.writeValueAsString(
            new UserCheckRequest(TEST_USER_EMAIL));

        this.mockMvc.perform(get("/check-joined")
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @Test
    @DisplayName("회원 가입 요청/응답 테스트")
    void join() throws Exception {
        given(userService.createUser(any())).willReturn(TEST_USER_ID);

        String inputJson = objectMapper.writeValueAsString(
            new UserCreateRequest(TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD,
                TEST_AREA_ID, TEST_AUTH_NUMBER));

        this.mockMvc.perform(post("/join")
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @Test
    @DisplayName("로그인 요청")
    void login() throws Exception {
        TokenResponse expected = new TokenResponse("fake Token");
        given(userService.login(any())).willReturn(expected);

        String inputJson = objectMapper.writeValueAsString(
            new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD));

        this.mockMvc.perform(post("/login")
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("accessToken").hasJsonPath())
            .andDo(print())
            .andReturn()
            .getResponse();
    }

    @Test
    @DisplayName("로그인 실패 - 이메일이 존재하지 않거나 비밀번호가 틀린 경우")
    void login_IfRequestValueIsWrong_ThrowException() throws Exception {
        WrongUserInputException expected = new WrongUserInputException("회원가입 실패");
        given(userService.login(any())).willThrow(expected);

        String inputJson = objectMapper.writeValueAsString(
            new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD));

        this.mockMvc.perform(post("/login")
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("errorMessage").hasJsonPath());
    }

    @Test
    @DisplayName("로그인 실패 - 이메일 형식이 잘못된 경우")
    void login_IfEmailFormatIsWrong_ThrowException() throws Exception {
        WrongUserInputException expected = new WrongUserInputException("회원가입 실패");
        given(userService.login(any())).willThrow(expected);

        String inputJson = objectMapper.writeValueAsString(
            new LoginRequest("@@@.@@", TEST_USER_PASSWORD));

        this.mockMvc.perform(post("/login")
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("errorMessage").hasJsonPath());
    }

    @Test
    @DisplayName("로그인 실패 - 패스워드 형식이 잘못된 경우")
    void login_IfPasswordFormatIsWrong_ThrowException() throws Exception {
        WrongUserInputException expected = new WrongUserInputException("회원가입 실패");
        given(userService.login(any())).willThrow(expected);

        String inputJson = objectMapper.writeValueAsString(
            new LoginRequest(TEST_USER_EMAIL, "aa"));

        this.mockMvc.perform(post("/login")
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("errorMessage").hasJsonPath());
    }

    @Test
    @DisplayName("로그인 기본 페이지 요청 - 401 반환")
    void login_GET_ReturnUnauthorized() throws Exception {
        this.mockMvc.perform(get("/login")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andDo(print())
            .andReturn()
            .getResponse();
    }

    @Test
    @DisplayName("내 정보 얻기")
    void findMe() throws Exception {
        UserResponse expected = UserResponse.builder()
            .id(TEST_USER_ID)
            .email(TEST_USER_EMAIL)
            .nickname(TEST_USER_NICKNAME)
            .build();
        given(userService.findMe()).willReturn(expected);

        String expectedJson = objectMapper.writeValueAsString(expected);

        String actual = this.mockMvc.perform(get("/users/me")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();

        assertThat(actual).isEqualTo(expectedJson);
    }

    @Test
    @DisplayName("내 정보 수정")
    void updateMe_Success() throws Exception {
        UserResponse expected = UserResponse.builder()
            .id(TEST_USER_ID)
            .email(TEST_USER_EMAIL)
            .nickname(TEST_USER_NICKNAME)
            .build();
        given(userService.updateMe(any())).willReturn(expected);

        String expectedJson = objectMapper.writeValueAsString(expected);

        String actual = this.mockMvc.perform(put("/users/me")
            .content(expectedJson)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();

        assertThat(actual).isEqualTo(expectedJson);
    }

    @Test
    @DisplayName("회원 탈퇴")
    void deleteUser() throws Exception {
        this.mockMvc.perform(delete("/users/me")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
}
