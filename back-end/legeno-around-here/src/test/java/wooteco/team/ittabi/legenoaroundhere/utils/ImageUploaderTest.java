package wooteco.team.ittabi.legenoaroundhere.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_CONTENT_TYPE;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_PASSWORD;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.domain.post.image.Image;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.exception.NotImageMimeTypeException;
import wooteco.team.ittabi.legenoaroundhere.service.ServiceTest;

public class ImageUploaderTest extends ServiceTest {

    @Autowired
    private ImageUploader imageUploader;

    @BeforeEach
    void setUp() {
        User user = createUser(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        setAuthentication(user);
    }

    @DisplayName("이미지 업로드 - 성공")
    @Test
    void upload_SuccessToSave() throws IOException {
        MultipartFile multipartFile = FileConverter
            .convert("right_image1.jpg", TEST_IMAGE_CONTENT_TYPE);

        Image image = imageUploader.uploadImage(multipartFile);

        assertThat(image.getName()).isEqualTo(multipartFile.getName());
    }

    @DisplayName("이미지 업로드 - 실패, 이미지 파일 MIME Type 유효성 검사")
    @Test
    void upload_NotImageMimeType_ThrownException() throws IOException {
        MultipartFile multipartFile = FileConverter
            .convert("not_image_mime_type.jpg", TEST_IMAGE_CONTENT_TYPE);

        assertThatThrownBy(() -> imageUploader.uploadImage(multipartFile))
            .isInstanceOf(NotImageMimeTypeException.class);
    }
}
