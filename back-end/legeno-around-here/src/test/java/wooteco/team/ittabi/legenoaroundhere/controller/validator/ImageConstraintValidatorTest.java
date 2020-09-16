
package wooteco.team.ittabi.legenoaroundhere.controller.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.team.ittabi.legenoaroundhere.controller.validator.ImageConstraintValidator.MAX_IMAGE_LENGTH;
import static wooteco.team.ittabi.legenoaroundhere.controller.validator.ImageConstraintValidator.MAX_IMAGE_MB_VOLUME;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.IMAGES_PATH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.POSTS_PATH;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_DIR;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_NAME;

import com.google.common.base.Charsets;
import io.restassured.internal.util.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import wooteco.team.ittabi.legenoaroundhere.controller.GlobalExceptionHandler;
import wooteco.team.ittabi.legenoaroundhere.controller.PostController;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ImageConstraintValidatorTest {

    @Autowired
    private PostController postController;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(postController)
            .setControllerAdvice(globalExceptionHandler)
            .build();
    }

    @Test
    public void uploadPostImages_OverImagesLength_ThrownException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(POSTS_PATH + IMAGES_PATH)
            .file("images", convertFileByteArray(TEST_IMAGE_NAME))
            .file("images", convertFileByteArray(TEST_IMAGE_NAME))
            .file("images", convertFileByteArray(TEST_IMAGE_NAME))
            .file("images", convertFileByteArray(TEST_IMAGE_NAME))
            .file("images", convertFileByteArray(TEST_IMAGE_NAME))
            .file("images", convertFileByteArray(TEST_IMAGE_NAME))
            .file("images", convertFileByteArray(TEST_IMAGE_NAME))
            .file("images", convertFileByteArray(TEST_IMAGE_NAME))
            .file("images", convertFileByteArray(TEST_IMAGE_NAME))
            .file("images", convertFileByteArray(TEST_IMAGE_NAME))
            .file("images", convertFileByteArray(TEST_IMAGE_NAME)))
            .andExpect(result -> assertThat(result.getResponse().getContentAsString(Charsets.UTF_8))
                .contains("이미지는 최대 " + MAX_IMAGE_LENGTH + "개까지 등록할 수 있습니다!"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void uploadPostImages_OverImagesVolume_ThrownException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(POSTS_PATH + IMAGES_PATH)
            .file("images", convertFileByteArray("image3_5MB.jpeg")))
            .andExpect(result -> assertThat(result.getResponse().getContentAsString(Charsets.UTF_8))
                .contains("이미지는 " + MAX_IMAGE_MB_VOLUME + "MB를 넘을 수 없습니다!"))
            .andExpect(status().isBadRequest());
    }

    private byte[] convertFileByteArray(String fileName) throws IOException {
        File file = new File(TEST_IMAGE_DIR + fileName);
        FileInputStream input = new FileInputStream(file);
        return IOUtils.toByteArray(input);
    }
}
