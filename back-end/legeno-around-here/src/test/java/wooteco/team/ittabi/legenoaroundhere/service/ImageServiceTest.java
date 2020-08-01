package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageTestConstants.TEST_IMAGE_CONTENT_TYPE;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostTestConstants.TEST_WRITING;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.aws.S3Uploader;
import wooteco.team.ittabi.legenoaroundhere.domain.Image;
import wooteco.team.ittabi.legenoaroundhere.domain.Post;
import wooteco.team.ittabi.legenoaroundhere.exception.NotImageMimeTypeException;
import wooteco.team.ittabi.legenoaroundhere.repository.ImageRepository;
import wooteco.team.ittabi.legenoaroundhere.utils.FileConverter;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class ImageServiceTest {

    @Autowired
    private ImageRepository imageRepository;

    @Mock
    private S3Uploader s3Uploader;

    private ImageService imageService;

    @BeforeEach
    void setUp() {
        imageService = new ImageService(imageRepository, s3Uploader);
    }

    @DisplayName("이미지 저장 - 성공")
    @Test
    void save_SuccessToSave() throws IOException {
        MultipartFile multipartFile = FileConverter
            .convert("right_image1.jpg", TEST_IMAGE_CONTENT_TYPE);

        Image image = imageService.save(multipartFile, new Post(TEST_WRITING));

        assertThat(image.getName()).isEqualTo(multipartFile.getName());
    }

    @DisplayName("이미지 저장 - 실패, 이미지 파일 MIME Type 유효성 검사")
    @Test
    void save_NotImageMimeType_ThrownException() throws IOException {
        MultipartFile multipartFile = FileConverter
            .convert("not_image_mime_type.jpg", TEST_IMAGE_CONTENT_TYPE);

        assertThatThrownBy(() -> imageService.save(multipartFile, new Post(TEST_WRITING)))
            .isInstanceOf(NotImageMimeTypeException.class);
    }
}