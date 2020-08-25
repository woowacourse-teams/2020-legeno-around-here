package wooteco.team.ittabi.legenoaroundhere.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_EMPTY_IMAGES;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_ID;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import wooteco.team.ittabi.legenoaroundhere.dto.PostCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostUpdateRequest;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAuthorizedException;
import wooteco.team.ittabi.legenoaroundhere.service.PostService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PostControllerTest {

    private static final String EXPECTED_WRITING = "Hello!!";
    private static final String ANY_ID = "1";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .build();
    }

    @Test
    void updatePost_IfNotAuthor_Forbidden() throws Exception {
        doThrow(NotAuthorizedException.class).when(postService).updatePost(any(), any());

        String inputJson = objectMapper.writeValueAsString(
            new PostUpdateRequest(EXPECTED_WRITING, TEST_EMPTY_IMAGES));

        this.mockMvc.perform(put("/posts/" + ANY_ID)
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("errorMessage").hasJsonPath());
    }

    @Test
    void deletePost_IfNotAuthor_Forbidden() throws Exception {
        doThrow(NotAuthorizedException.class).when(postService).deletePost(any());

        String inputJson = objectMapper.writeValueAsString(
            new PostCreateRequest(EXPECTED_WRITING, TEST_EMPTY_IMAGES, TEST_AREA_ID,
                TEST_SECTOR_ID));

        this.mockMvc.perform(delete("/posts/" + ANY_ID)
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("errorMessage").hasJsonPath());
    }
}
