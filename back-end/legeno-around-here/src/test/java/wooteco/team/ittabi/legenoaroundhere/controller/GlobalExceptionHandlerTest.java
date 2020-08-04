package wooteco.team.ittabi.legenoaroundhere.controller;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAuthorizedException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.exception.UserInputException;
import wooteco.team.ittabi.legenoaroundhere.service.SectorService;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

    @MockBean
    private SectorService sectorService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .build();
    }

    @Test
    void handleNotFound() throws Exception {
        doThrow(NotExistsException.class).when(sectorService).findAllUsedSector();

        this.mockMvc.perform(get("/sectors/")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("errorMessage").hasJsonPath());
    }

    @Test
    void handleBadRequest() throws Exception {
        doThrow(UserInputException.class).when(sectorService).findAllUsedSector();

        this.mockMvc.perform(get("/sectors/")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("errorMessage").hasJsonPath());
    }

    @Test
    void handleForbidden() throws Exception {
        doThrow(NotAuthorizedException.class).when(sectorService).findAllUsedSector();

        this.mockMvc.perform(get("/sectors/")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("errorMessage").hasJsonPath());
    }

    @Test
    void handleInternalServerError() throws Exception {
        doThrow(RuntimeException.class).when(sectorService).findAllUsedSector();

        this.mockMvc.perform(get("/sectors/")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("errorMessage").hasJsonPath());
    }
}