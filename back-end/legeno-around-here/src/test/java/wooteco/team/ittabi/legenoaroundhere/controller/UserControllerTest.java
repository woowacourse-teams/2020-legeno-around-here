package wooteco.team.ittabi.legenoaroundhere.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_MY_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_PASSWORD;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import wooteco.team.ittabi.legenoaroundhere.dto.LoginRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.UserInputException;
import wooteco.team.ittabi.legenoaroundhere.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    private UserService userService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .build();
    }

    @Test
    @DisplayName("회원 가입 요청/응답 테스트")
    void join() throws Exception {
        given(userService.createUser(any())).willReturn(TEST_ID);

        String inputJson = objectMapper.writeValueAsString(
            new UserCreateRequest(TEST_MY_EMAIL, TEST_NICKNAME, TEST_PASSWORD));

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
            new LoginRequest(TEST_MY_EMAIL, TEST_PASSWORD));

        MockHttpServletResponse response = this.mockMvc.perform(post("/login")
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
        UserInputException expected = new UserInputException("회원가입 실패");
        given(userService.login(any())).willThrow(expected);

        String inputJson = objectMapper.writeValueAsString(
            new LoginRequest(TEST_MY_EMAIL, TEST_PASSWORD));

        this.mockMvc.perform(post("/login")
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("errorMessage").hasJsonPath());
    }

    @Test
    @DisplayName("로그인 실패 - 요청 형식 자체가 잘못된 경우")
    void login_IfRequestFormatIsWrong_ThrowException() {
        // Todo: ExceptionHandler로 처리하도록 구현한 뒤, 이 테스트도 구현해야함
    }

    @Test
    @DisplayName("내 정보 얻기")
    void findUser() throws Exception {
        UserResponse expected = new UserResponse(TEST_ID, TEST_MY_EMAIL, TEST_NICKNAME);
        given(userService.findUser(any())).willReturn(expected);

        String expectedJson = objectMapper.writeValueAsString(expected);

        String actual = this.mockMvc.perform(get("/users/myinfo")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();

        assertThat(actual).isEqualTo(expectedJson);
    }
}
