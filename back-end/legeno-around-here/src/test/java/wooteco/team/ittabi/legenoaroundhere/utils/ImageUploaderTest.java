package wooteco.team.ittabi.legenoaroundhere.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AUTH_NUMBER;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_CONTENT_TYPE;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_NOT_MIME_TYPE_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_OTHER_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.domain.post.image.PostImage;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.domain.user.UserImage;
import wooteco.team.ittabi.legenoaroundhere.domain.user.mailauth.MailAuth;
import wooteco.team.ittabi.legenoaroundhere.exception.NotImageMimeTypeException;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;
import wooteco.team.ittabi.legenoaroundhere.repository.MailAuthRepository;
import wooteco.team.ittabi.legenoaroundhere.service.ServiceTest;

public class ImageUploaderTest extends ServiceTest {

    @MockBean
    private MailAuthRepository mailAuthRepository;

    @Autowired
    private ImageUploader imageUploader;

    @BeforeEach
    void setUp() {
        MailAuth mailAuth = new MailAuth(TEST_USER_EMAIL, TEST_AUTH_NUMBER);
        when(mailAuthRepository.findByEmail(any())).thenReturn(java.util.Optional.of(mailAuth));

        User user = createUser("image_" + TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        setAuthentication(user);
    }

    @DisplayName("포스트 이미지들 업로드 - 성공")
    @Test
    void uploadPostImages_SuccessToSave() throws IOException {
        ArrayList<MultipartFile> multipartFiles = new ArrayList<>();
        multipartFiles.add(TestConverterUtils.convert(TEST_IMAGE_NAME, TEST_IMAGE_CONTENT_TYPE));
        multipartFiles
            .add(TestConverterUtils.convert(TEST_IMAGE_OTHER_NAME, TEST_IMAGE_CONTENT_TYPE));

        List<PostImage> postImages = imageUploader.uploadPostImages(multipartFiles);
        List<String> userName = postImages.stream()
            .map(PostImage::getName)
            .collect(Collectors.toList());

        assertThat(postImages).hasSize(2);
        assertThat(userName).contains(TEST_IMAGE_NAME);
        assertThat(userName).contains(TEST_IMAGE_OTHER_NAME);
    }

    @DisplayName("포스트 이미지들 업로드 - 빈 값 리턴, Null이거나 Empty일 때")
    @Test
    void uploadPostImages_NullOrEmpty_ReturnEmpty() {
        List<PostImage> nullImages = imageUploader.uploadPostImages(null);
        List<PostImage> emptyImages = imageUploader.uploadPostImages(new ArrayList<>());

        assertThat(nullImages.isEmpty()).isTrue();
        assertThat(emptyImages.isEmpty()).isTrue();
    }

    @DisplayName("포스트 이미지들 업로드 - 실패, 이미지 파일 MIME Type 유효성 검사")
    @Test
    void uploadPostImages_NotImageMimeType_ThrownException() throws IOException {
        ArrayList<MultipartFile> multipartFiles = new ArrayList<>();
        multipartFiles.add(TestConverterUtils.convert(TEST_IMAGE_NAME, TEST_IMAGE_CONTENT_TYPE));
        multipartFiles
            .add(
                TestConverterUtils.convert(TEST_IMAGE_NOT_MIME_TYPE_NAME, TEST_IMAGE_CONTENT_TYPE));

        assertThatThrownBy(() -> imageUploader.uploadPostImages(multipartFiles))
            .isInstanceOf(NotImageMimeTypeException.class);
    }

    @DisplayName("포스트 이미지 업로드 - 성공")
    @Test
    void uploadPostImage_SuccessToSave() throws IOException {
        MultipartFile multipartFile = TestConverterUtils
            .convert(TEST_IMAGE_NAME, TEST_IMAGE_CONTENT_TYPE);

        PostImage postImage = imageUploader.uploadPostImage(multipartFile);

        assertThat(postImage.getName()).isEqualTo(multipartFile.getName());
    }

    @DisplayName("포스트 이미지 업로드 - 빈 값 리턴, Null일 때")
    @Test
    void uploadPostImage_Null_ReturnEmpty() {
        PostImage postImage = imageUploader.uploadPostImage(null);

        assertThat(postImage).isNull();
    }

    @DisplayName("포스트 이미지 업로드 - 실패, 이미지 파일 MIME Type 유효성 검사")
    @Test
    void uploadPostImage_NotImageMimeType_ThrownException() throws IOException {
        MultipartFile multipartFile = TestConverterUtils
            .convert(TEST_IMAGE_NOT_MIME_TYPE_NAME, TEST_IMAGE_CONTENT_TYPE);

        assertThatThrownBy(() -> imageUploader.uploadPostImage(multipartFile))
            .isInstanceOf(NotImageMimeTypeException.class);
    }

    @DisplayName("사용자 이미지 업로드 - 성공")
    @Test
    void uploadUserImage_SuccessToSave() throws IOException {
        MultipartFile multipartFile = TestConverterUtils
            .convert(TEST_IMAGE_NAME, TEST_IMAGE_CONTENT_TYPE);

        UserImage userImage = imageUploader.uploadUserImage(multipartFile);

        assertThat(userImage.getName()).isEqualTo(multipartFile.getName());
    }

    @DisplayName("포스트 이미지 업로드 - 예외 반환, Null일 때")
    @Test
    void uploadUserImage_Null_ThrownException() {
        assertThatThrownBy(() -> imageUploader.uploadUserImage(null))
            .isInstanceOf(WrongUserInputException.class);
    }

    @DisplayName("사용자 이미지 업로드 - 실패, 이미지 파일 MIME Type 유효성 검사")
    @Test
    void uploadUserImage_NotImageMimeType_ThrownException() throws IOException {
        MultipartFile multipartFile = TestConverterUtils
            .convert(TEST_IMAGE_NOT_MIME_TYPE_NAME, TEST_IMAGE_CONTENT_TYPE);

        assertThatThrownBy(() -> imageUploader.uploadUserImage(multipartFile))
            .isInstanceOf(NotImageMimeTypeException.class);
    }
}
