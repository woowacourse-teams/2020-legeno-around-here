package wooteco.team.ittabi.legenoaroundhere.domain.post;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_NOT_TXT_EXTENSION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_TEXT_CONTENT_TYPE;

import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.domain.util.ImageExtension;
import wooteco.team.ittabi.legenoaroundhere.exception.NotImageExtensionException;
import wooteco.team.ittabi.legenoaroundhere.utils.TestConverterUtils;

public class PostImageExtensionTest {

    @DisplayName("이미지 확장자 유효성 검사 - 실패, 예외 처리")
    @Test
    void validateImageExtension_NoneMatchImageExtension_ThrownException() throws IOException {
        MultipartFile multipartFile = TestConverterUtils
            .convert(TEST_IMAGE_NOT_TXT_EXTENSION, TEST_IMAGE_TEXT_CONTENT_TYPE);

        assertThatThrownBy(() -> ImageExtension.validateImageExtension(multipartFile))
            .isInstanceOf(NotImageExtensionException.class);
    }
}
