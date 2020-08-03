package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_PASSWORD;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.PostRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAuthorizedException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.infra.JwtTokenGenerator;
import wooteco.team.ittabi.legenoaroundhere.repository.UserRepository;

@DataJpaTest
@Import({PostService.class, UserService.class, JwtTokenGenerator.class})
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private final String expectedWriting = "Hello!!";
    private User user;
    private User another;

    @BeforeEach
    void setUp() {
        user = createUser(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        another = createUser("another@test.com", TEST_NICKNAME, TEST_PASSWORD);
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

    @DisplayName("포스트 생성 - 성공")
    @Test
    void createPost_SuccessToCreate() {
        PostRequest postRequest = new PostRequest(expectedWriting);

        PostResponse postResponse = postService.createPost(postRequest);

        assertThat(postResponse.getId()).isNotNull();
        assertThat(postResponse.getWriting()).isEqualTo(expectedWriting);
        assertThat(postResponse.getUser()).isEqualTo(UserResponse.from(user));
    }

    @DisplayName("ID로 포스트 조회 - 성공")
    @Test
    void findPost_HasId_SuccessToFind() {
        PostRequest postRequest = new PostRequest(expectedWriting);
        PostResponse createdPostResponse = postService.createPost(postRequest);

        PostResponse postResponse = postService
            .findPost(createdPostResponse.getId());

        assertThat(postResponse.getId()).isEqualTo(createdPostResponse.getId());
        assertThat(postResponse.getWriting()).isEqualTo(createdPostResponse.getWriting());
        assertThat(postResponse.getUser()).isEqualTo(UserResponse.from(user));
    }

    @DisplayName("ID로 포스트 조회 - 실패")
    @Test
    void findPost_HasNotId_ThrownException() {
        Long invalidId = -1L;

        assertThatThrownBy(() -> postService.findPost(invalidId))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("포스트 전체 목록 조회 - 성공")
    @Test
    void findAllPost_SuccessToFind() {
        PostRequest postRequest = new PostRequest(expectedWriting);

        postService.createPost(postRequest);
        postService.createPost(postRequest);

        List<PostResponse> posts = postService.findAllPost();

        assertThat(posts).hasSize(2);
    }

    @DisplayName("ID로 포스트 수정 - 성공")
    @Test
    void updatePost_HasId_SuccessToUpdate() {
        String updatedPostWriting = "Jamie and BingBong";
        PostRequest createdPostRequest = new PostRequest(expectedWriting);
        PostResponse createdPostResponse = postService
            .createPost(createdPostRequest);
        PostRequest updatedPostRequest = new PostRequest(updatedPostWriting);

        postService.updatePost(createdPostResponse.getId(), updatedPostRequest);
        PostResponse updatedPostResponse = postService
            .findPost(createdPostResponse.getId());

        assertThat(updatedPostResponse.getWriting()).isEqualTo(updatedPostWriting);
        assertThat(updatedPostResponse.getUser()).isEqualTo(UserResponse.from(user));
    }

    @DisplayName("ID로 포스트 수정 - 실패")
    @Test
    void updatePost_HasNotId_ThrownException() {
        PostRequest postRequest = new PostRequest(expectedWriting);
        Long invalidId = -1L;

        assertThatThrownBy(() -> postService.updatePost(invalidId, postRequest))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("ID로 포스트 수정 - 예외 발생, 작성자가 아님")
    @Test
    void updatePost_IfNotAuthor_ThrowException() {
        String updatedPostWriting = "Jamie and BingBong";
        PostRequest createdPostRequest = new PostRequest(expectedWriting);
        PostResponse createdPostResponse = postService.createPost(createdPostRequest);
        PostRequest updatedPostRequest = new PostRequest(updatedPostWriting);

        setAuthentication(another);
        assertThatThrownBy(() -> postService
            .updatePost(createdPostResponse.getId(), updatedPostRequest))
            .isInstanceOf(NotAuthorizedException.class);
    }

    @DisplayName("ID로 포스트 삭제 - 성공")
    @Test
    void deletePost_HasId_SuccessToDelete() {
        PostRequest createdPostRequest = new PostRequest(expectedWriting);
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
        PostRequest createdPostRequest = new PostRequest(expectedWriting);

        PostResponse createdPostResponse = postService.createPost(createdPostRequest);

        postService.deletePost(createdPostResponse.getId());

        assertThatThrownBy(
            () -> postService.deletePost(createdPostResponse.getId()))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("ID로 포스트 삭제 - 실패, 다른 사용자")
    @Test
    void deletePost_IfNotAuthor_ThrowException() {
        PostRequest createdPostRequest = new PostRequest(expectedWriting);
        PostResponse createdPostResponse = postService.createPost(createdPostRequest);

        setAuthentication(another);
        assertThatThrownBy(
            () -> postService.deletePost(createdPostResponse.getId()))
            .isInstanceOf(NotAuthorizedException.class);
    }
}
