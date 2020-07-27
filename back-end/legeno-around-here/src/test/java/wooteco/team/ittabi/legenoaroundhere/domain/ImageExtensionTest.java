package wooteco.team.ittabi.legenoaroundhere.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.restassured.internal.util.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.exception.NotImageExtensionException;

public class ImageExtensionTest {

    @DisplayName("이미지 확장자 유효성 검사")
    @Test
    void validateImageExtension_NoneMatchImageExtension_ThrownException() throws IOException {
        File file = new File("src/test/resources/static/images/test4.txt");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("test4.txt",
            file.getName(), "text/plain", IOUtils.toByteArray(input));

        assertThatThrownBy(() -> ImageExtension.validateImageExtension(multipartFile))
            .isInstanceOf(NotImageExtensionException.class);

    }
}
