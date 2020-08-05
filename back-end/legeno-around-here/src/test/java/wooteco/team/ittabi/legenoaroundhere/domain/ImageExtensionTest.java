package wooteco.team.ittabi.legenoaroundhere.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageTestConstants.TEST_TEXT_CONTENT_TYPE;

import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.exception.NotImageExtensionException;
import wooteco.team.ittabi.legenoaroundhere.utils.FileConverter;

public class ImageExtensionTest {

    @DisplayName("이미지 확장자 유효성 검사 - 실패, 예외 처리")
    @Test
    void validateImageExtension_NoneMatchImageExtension_ThrownException() throws IOException {
        MultipartFile multipartFile = FileConverter
            .convert("not_image_extension.txt", TEST_TEXT_CONTENT_TYPE);

        assertThatThrownBy(() -> ImageExtension.validateImageExtension(multipartFile))
            .isInstanceOf(NotImageExtensionException.class);
    }
}
