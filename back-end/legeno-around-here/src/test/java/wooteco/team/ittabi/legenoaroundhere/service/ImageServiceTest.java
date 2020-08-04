package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageTestConstants.TEST_IMAGE_CONTENT_TYPE;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_MY_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_PASSWORD;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.aws.S3Uploader;
import wooteco.team.ittabi.legenoaroundhere.domain.Image;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.exception.NotImageMimeTypeException;
import wooteco.team.ittabi.legenoaroundhere.utils.FileConverter;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest extends AuthServiceTest {

    @Mock
    private S3Uploader s3Uploader;

    private ImageService imageService;

    @BeforeEach
    void setUp() {
        imageService = new ImageService(s3Uploader);
        User user = createUser(TEST_MY_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        setAuthentication(user);
    }

    @DisplayName("이미지 업로드 - 성공")
    @Test
    void upload_SuccessToSave() throws IOException {
        MultipartFile multipartFile = FileConverter
            .convert("right_image1.jpg", TEST_IMAGE_CONTENT_TYPE);

        Image image = imageService.upload(multipartFile);

        assertThat(image.getName()).isEqualTo(multipartFile.getName());
    }

    @DisplayName("이미지 업로드 - 실패, 이미지 파일 MIME Type 유효성 검사")
    @Test
    void upload_NotImageMimeType_ThrownException() throws IOException {
        MultipartFile multipartFile = FileConverter
            .convert("not_image_mime_type.jpg", TEST_IMAGE_CONTENT_TYPE);

        assertThatThrownBy(() -> imageService.upload(multipartFile))
            .isInstanceOf(NotImageMimeTypeException.class);
    }
}
