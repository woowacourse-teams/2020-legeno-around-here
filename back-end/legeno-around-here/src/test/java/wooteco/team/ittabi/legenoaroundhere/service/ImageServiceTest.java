package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageTestConstants.TEST_IMAGE_CONTENT_TYPE;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_MY_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_PASSWORD;

import java.io.IOException;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.aws.S3Uploader;
import wooteco.team.ittabi.legenoaroundhere.domain.Image;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.UserCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotImageMimeTypeException;
import wooteco.team.ittabi.legenoaroundhere.infra.JwtTokenGenerator;
import wooteco.team.ittabi.legenoaroundhere.repository.UserRepository;
import wooteco.team.ittabi.legenoaroundhere.utils.FileConverter;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Import({UserService.class, JwtTokenGenerator.class})
@Transactional
public class ImageServiceTest {

    @Mock
    private S3Uploader s3Uploader;

    private ImageService imageService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        imageService = new ImageService(s3Uploader);
        User user = createUser(TEST_MY_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        setAuthentication(user);
    }

    private User createUser(String email, String nickname, String password) {
        UserCreateRequest userCreateRequest = new UserCreateRequest(email, nickname, password);
        Long userId = userService.createUser(userCreateRequest);

        return userRepository.findById(userId)
            .orElseThrow(() -> new NotExistsException("해당하는 사용자가 존재하지 않습니다."));
    }

    private void setAuthentication(User user) {
        UserDetails userDetails = userService.loadUserByUsername(user.getEmailByString());
        Authentication authToken = new UsernamePasswordAuthenticationToken(
            user, "TestCredentials", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
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
