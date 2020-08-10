package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageTestConstants.EMPTY_MULTIPART_FILES;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageTestConstants.TEST_IMAGE_CONTENT_TYPE;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostTestConstants.TEST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_ANOTHER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_PASSWORD;

import java.io.IOException;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.PostRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostWithCommentsCountResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAuthorizedException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.utils.FileConverter;

public class PostServiceTest extends ServiceTest {

    private User user;
    private User another;

    @Autowired
    private PostService postService;

    @BeforeEach
    void setUp() {
        user = createUser(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        another = createUser(TEST_ANOTHER_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        setAuthentication(user);
    }

    @DisplayName("이미지를 포함하지 않는 포스트 생성 - 성공")
    @Test
    void createPostWithoutImage_SuccessToCreate() {
        PostRequest postRequest = new PostRequest(TEST_WRITING, EMPTY_MULTIPART_FILES);

        PostResponse postResponse = postService.createPost(postRequest);

        assertThat(postResponse.getId()).isNotNull();
        assertThat(postResponse.getWriting()).isEqualTo(TEST_WRITING);
        assertThat(postResponse.getCreator()).isEqualTo(UserResponse.from(user));
    }

    @DisplayName("이미지를 포함한 포스트 생성 - 성공")
    @Test
    void createPostWithImage_SuccessToCreate() throws IOException {
        MultipartFile multipartFile = FileConverter
            .convert("right_image1.jpg", TEST_IMAGE_CONTENT_TYPE);
        PostRequest postRequest = new PostRequest(TEST_WRITING,
            Collections.singletonList(multipartFile));

        PostResponse postResponse = postService.createPost(postRequest);

        assertThat(postResponse.getWriting()).isEqualTo(TEST_WRITING);
        assertThat(postResponse.getImages()).hasSize(1);
        assertThat(postResponse.getCreator()).isEqualTo(UserResponse.from(user));
    }

    @DisplayName("ID로 포스트 조회 - 성공")
    @Test
    void findPost_HasId_SuccessToFind() {
        PostRequest postRequest = new PostRequest(TEST_WRITING, EMPTY_MULTIPART_FILES);
        PostResponse createdPostResponse = postService.createPost(postRequest);

        PostResponse postResponse = postService.findPost(createdPostResponse.getId());

        assertThat(postResponse.getId()).isEqualTo(createdPostResponse.getId());
        assertThat(postResponse.getWriting()).isEqualTo(createdPostResponse.getWriting());
        assertThat(postResponse.getCreator()).isEqualTo(UserResponse.from(user));
    }

    @DisplayName("ID로 포스트 조회 - 실패, 이미 지워진 포스트")
    @Test
    void findPost_AlreadyDeletedPost_ThrownException() {
        PostRequest postRequest = new PostRequest(TEST_WRITING, EMPTY_MULTIPART_FILES);
        PostResponse createdPostResponse = postService.createPost(postRequest);
        postService.deletePost(createdPostResponse.getId());

        assertThatThrownBy(() -> postService.findPost(createdPostResponse.getId()))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("ID로 포스트 조회 - 실패, 유효하지 않은 포스트 ID")
    @Test
    void findPost_HasNotId_ThrownException() {
        Long invalidId = -1L;

        assertThatThrownBy(() -> postService.findPost(invalidId))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("포스트 전체 목록 조회 - 성공")
    @Test
    void findAllPost_SuccessToFind() {
        PostRequest postRequest = new PostRequest(TEST_WRITING, EMPTY_MULTIPART_FILES);
        postService.createPost(postRequest);
        postService.createPost(postRequest);

        Page<PostWithCommentsCountResponse> posts = postService.findAllPost(Pageable.unpaged());

        assertThat(posts.getContent()).hasSize(2);
    }

    @DisplayName("ID로 포스트 수정 - 성공")
    @Test
    void updatePost_HasId_SuccessToUpdate() {
        String updatedPostWriting = "Jamie and BingBong";
        PostRequest createdPostRequest = new PostRequest(TEST_WRITING, EMPTY_MULTIPART_FILES);
        PostResponse createdPostResponse = postService.createPost(createdPostRequest);
        PostRequest updatedPostRequest = new PostRequest(updatedPostWriting, EMPTY_MULTIPART_FILES);

        postService.updatePost(createdPostResponse.getId(), updatedPostRequest);
        PostResponse updatedPostResponse = postService
            .findPost(createdPostResponse.getId());

        assertThat(updatedPostResponse.getWriting()).isEqualTo(updatedPostWriting);
        assertThat(updatedPostResponse.getCreator()).isEqualTo(UserResponse.from(user));
    }

    @DisplayName("ID로 포스트 수정 - 실패")
    @Test
    void updatePost_HasNotId_ThrownException() {
        PostRequest postRequest = new PostRequest(TEST_WRITING, EMPTY_MULTIPART_FILES);
        Long invalidId = -1L;

        assertThatThrownBy(() -> postService.updatePost(invalidId, postRequest))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("ID로 포스트 수정 - 예외 발생, 작성자가 아님")
    @Test
    void updatePost_IfNotCreator_ThrowException() {
        String updatedPostWriting = "Jamie and BingBong";
        PostRequest createdPostRequest = new PostRequest(TEST_WRITING, EMPTY_MULTIPART_FILES);
        PostResponse createdPostResponse = postService.createPost(createdPostRequest);
        PostRequest updatedPostRequest = new PostRequest(updatedPostWriting, EMPTY_MULTIPART_FILES);

        setAuthentication(another);
        assertThatThrownBy(() -> postService
            .updatePost(createdPostResponse.getId(), updatedPostRequest))
            .isInstanceOf(NotAuthorizedException.class);
    }

    @DisplayName("ID로 포스트 삭제 - 성공")
    @Test
    void deletePost_HasId_SuccessToDelete() {
        PostRequest createdPostRequest = new PostRequest(TEST_WRITING, EMPTY_MULTIPART_FILES);
        PostResponse createdPostResponse = postService.createPost(createdPostRequest);

        postService.deletePost(createdPostResponse.getId());

        assertThatThrownBy(() -> postService.findPost(createdPostResponse.getId()))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("ID로 포스트 삭제 - 실패")
    @Test
    void deletePost_HasNotId_ThrownException() {
        Long invalidId = -1L;

        assertThatThrownBy(() -> postService.deletePost(invalidId))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("삭제한 ID로 포스트 삭제 - 실패")
    @Test
    void deletePost_AlreadyDeletedId_ThrownException() {
        PostRequest createdPostRequest = new PostRequest(TEST_WRITING, EMPTY_MULTIPART_FILES);
        PostResponse createdPostResponse = postService.createPost(createdPostRequest);

        postService.deletePost(createdPostResponse.getId());

        assertThatThrownBy(() -> postService.deletePost(createdPostResponse.getId()))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("ID로 포스트 삭제 - 예외 발생, 작성자가 아님")
    @Test
    void deletePost_IfNotCreator_ThrowException() {
        PostRequest createdPostRequest = new PostRequest(TEST_WRITING, EMPTY_MULTIPART_FILES);
        PostResponse createdPostResponse = postService.createPost(createdPostRequest);

        setAuthentication(another);
        assertThatThrownBy(
            () -> postService.deletePost(createdPostResponse.getId()))
            .isInstanceOf(NotAuthorizedException.class);
    }
}
