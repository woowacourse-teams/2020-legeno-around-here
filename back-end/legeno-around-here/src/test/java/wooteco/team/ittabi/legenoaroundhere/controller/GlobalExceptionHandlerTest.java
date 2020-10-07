package wooteco.team.ittabi.legenoaroundhere.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import wooteco.team.ittabi.legenoaroundhere.exception.FileIOException;
import wooteco.team.ittabi.legenoaroundhere.exception.LoginPageRedirectException;
import wooteco.team.ittabi.legenoaroundhere.exception.MultipartFileConvertException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAuthorizedException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotFoundAlgorithmException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotImageExtensionException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotImageMimeTypeException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotUniqueException;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;
import wooteco.team.ittabi.legenoaroundhere.service.SectorService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @MockBean
    private SectorService sectorService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .build();
    }

    @DisplayName("NotExistsException일 때, NotFound 리턴")
    @Test
    void handleNotFound_NotExistsException_NotFound() throws Exception {
        doThrow(NotExistsException.class)
            .when(sectorService).searchAvailableSectors(any(), any());

        this.mockMvc.perform(get("/sectors")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("errorMessage").hasJsonPath());
    }

    @DisplayName("UserInputException일 때, BadRequest 리턴")
    @Test
    void handleBadRequest_UserInputException_BadRequest() throws Exception {
        doThrow(WrongUserInputException.class)
            .when(sectorService).searchAvailableSectors(any(), any());
        this.mockMvc.perform(get("/sectors")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("errorMessage").hasJsonPath());
    }

    @DisplayName("NotImageMimeTypeException일 때, BadRequest 리턴")
    @Test
    void handleBadRequest_NotImageMimeTypeException_BadRequest() throws Exception {
        doThrow(NotImageMimeTypeException.class)
            .when(sectorService).searchAvailableSectors(any(), any());

        this.mockMvc.perform(get("/sectors")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("errorMessage").hasJsonPath());
    }

    @DisplayName("NotImageExtensionException일 때, BadRequest 리턴")
    @Test
    void handleBadRequest_NotImageExtensionException_BadRequest() throws Exception {
        doThrow(NotImageExtensionException.class)
            .when(sectorService).searchAvailableSectors(any(), any());

        this.mockMvc.perform(get("/sectors")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("errorMessage").hasJsonPath());
    }

    @DisplayName("MultipartFileConvertException일 때, BadRequest 리턴")
    @Test
    void handleBadRequest_MultipartFileConvertException_BadRequest() throws Exception {
        doThrow(MultipartFileConvertException.class)
            .when(sectorService).searchAvailableSectors(any(), any());

        this.mockMvc.perform(get("/sectors")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("errorMessage").hasJsonPath());
    }

    @DisplayName("FileIOException일 때, BadRequest 리턴")
    @Test
    void handleBadRequest_FileIOException_BadRequest() throws Exception {
        doThrow(FileIOException.class)
            .when(sectorService).searchAvailableSectors(any(), any());

        this.mockMvc.perform(get("/sectors")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("errorMessage").hasJsonPath());
    }

    @DisplayName("NotUniqueException일 때, BadRequest 리턴")
    @Test
    void handleBadRequest_NotUniqueException_BadRequest() throws Exception {
        doThrow(NotUniqueException.class)
            .when(sectorService).searchAvailableSectors(any(), any());

        this.mockMvc.perform(get("/sectors/")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("errorMessage").hasJsonPath());
    }

    @DisplayName("UserInputException일 때, Forbidden 리턴")
    @Test
    void handleForbidden_NotAuthorizedException_Forbidden() throws Exception {
        doThrow(NotAuthorizedException.class)
            .when(sectorService).searchAvailableSectors(any(), any());

        this.mockMvc.perform(get("/sectors")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("errorMessage").hasJsonPath());
    }

    @DisplayName("NotFoundAlgorithmException 때, InternalServerError 리턴")
    @Test
    void handleInternalServerError_NotFoundAlgorithmException_InternalServerError()
        throws Exception {
        doThrow(NotFoundAlgorithmException.class)
            .when(sectorService).searchAvailableSectors(any(), any());

        this.mockMvc.perform(get("/sectors/")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("errorMessage").hasJsonPath());
    }

    @DisplayName("LoginPageRedirectException 때, Unauthorized 리턴")
    @Test
    void handleInternalServerError_LoginPageRedirectException_Unauthorized() throws Exception {
        doThrow(LoginPageRedirectException.class)
            .when(sectorService).searchAvailableSectors(any(), any());

        this.mockMvc.perform(get("/sectors/")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("errorMessage").hasJsonPath());
    }

    @DisplayName("RuntimeException 때, InternalServerError 리턴")
    @Test
    void handleInternalServerError_RuntimeException_InternalServerError() throws Exception {
        doThrow(RuntimeException.class)
            .when(sectorService).searchAvailableSectors(any(), any());

        this.mockMvc.perform(get("/sectors/")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("errorMessage").hasJsonPath());
    }
}
