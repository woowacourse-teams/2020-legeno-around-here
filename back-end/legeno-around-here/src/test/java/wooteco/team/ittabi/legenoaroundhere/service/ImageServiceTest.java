package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.restassured.internal.util.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.domain.Post;
import wooteco.team.ittabi.legenoaroundhere.exception.NotImageMimeTypeException;

@DataJpaTest
@Import(ImageService.class)
public class ImageServiceTest {

    @Autowired
    private ImageService imageService;

    @DisplayName("이미지 파일 MIME Type 유효성 검사")
    @Test
    void validateImageMimeType_NotImageMimeType_ThrownException() throws IOException {

        File file = new File("src/test/resources/static/images/test3.jpg");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("test3.jpg",
            file.getName(), "image/jpg", IOUtils.toByteArray(input));

        Post post = new Post("writing");

        assertThatThrownBy(() -> imageService.save(multipartFile, post))
            .isInstanceOf(NotImageMimeTypeException.class);
    }
}
