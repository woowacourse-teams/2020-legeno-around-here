package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_PASSWORD;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Email;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Nickname;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Password;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.PostRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.infra.JwtTokenGenerator;

@DataJpaTest
@Import({PostService.class, UserService.class, JwtTokenGenerator.class})
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    private final String expectedWriting = "Hello!!";
    private User user;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        user = createUser();

        authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
    }

    private User createUser() {
        UserCreateRequest userCreateRequest =
            new UserCreateRequest(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        Long userId = userService.createUser(userCreateRequest);

        return new User(
            userId,
            new Email(TEST_EMAIL),
            new Nickname(TEST_NICKNAME),
            new Password(TEST_PASSWORD),
            new ArrayList<>()
        );
    }

    @DisplayName("포스트 생성 - 성공")
    @Test
    void createPost_SuccessToCreate() {
        PostRequest postRequest = new PostRequest(expectedWriting);

        PostResponse postResponse = postService.createPost(authentication, postRequest);

        assertThat(postResponse.getId()).isNotNull();
        assertThat(postResponse.getWriting()).isEqualTo(expectedWriting);
    }

    @DisplayName("ID로 포스트 조회 - 성공")
    @Test
    void findPost_HasId_SuccessToFind() {
        PostRequest postRequest = new PostRequest(expectedWriting);
        PostResponse createdPostResponse = postService.createPost(authentication, postRequest);

        PostResponse postResponse = postService
            .findPost(authentication, createdPostResponse.getId());

        assertThat(postResponse.getId()).isEqualTo(createdPostResponse.getId());
        assertThat(postResponse.getWriting()).isEqualTo(createdPostResponse.getWriting());
    }

    @DisplayName("ID로 포스트 조회 - 실패")
    @Test
    void findPost_HasNotId_ThrownException() {
        Long invalidId = -1L;

        assertThatThrownBy(() -> postService.findPost(authentication, invalidId))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("포스트 전체 목록 조회 - 성공")
    @Test
    void findAllPost_SuccessToFind() {
        PostRequest postRequest = new PostRequest(expectedWriting);
        postService.createPost(authentication, postRequest);
        postService.createPost(authentication, postRequest);

        List<PostResponse> posts = postService.findAllPost(authentication);

        assertThat(posts).hasSize(2);
    }

    @DisplayName("ID로 포스트 수정 - 성공")
    @Test
    void updatePost_HasId_SuccessToUpdate() {
        String updatedPostWriting = "Jamie and BingBong";
        PostRequest createdPostRequest = new PostRequest(expectedWriting);
        PostResponse createdPostResponse = postService
            .createPost(authentication, createdPostRequest);
        PostRequest updatedPostRequest = new PostRequest(updatedPostWriting);

        postService.updatePost(authentication, createdPostResponse.getId(), updatedPostRequest);
        PostResponse updatedPostResponse = postService
            .findPost(authentication, createdPostResponse.getId());

        assertThat(updatedPostResponse.getWriting()).isEqualTo(updatedPostWriting);
    }

    @DisplayName("ID로 포스트 수정 - 실패")
    @Test
    void updatePost_HasNotId_ThrownException() {
        PostRequest postRequest = new PostRequest(expectedWriting);
        Long invalidId = -1L;

        assertThatThrownBy(() -> postService.updatePost(authentication, invalidId, postRequest))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("ID로 포스트 삭제 - 성공")
    @Test
    void deletePost_HasId_SuccessToDelete() {
        PostRequest createdPostRequest = new PostRequest(expectedWriting);
        PostResponse createdPostResponse = postService
            .createPost(authentication, createdPostRequest);

        postService.deletePost(authentication, createdPostResponse.getId());

        assertThatThrownBy(() -> postService.findPost(authentication, createdPostResponse.getId()))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("ID로 포스트 삭제 - 실패")
    @Test
    void deletePost_HasNotId_ThrownException() {
        Long invalidId = -1L;

        assertThatThrownBy(() -> postService.deletePost(authentication, invalidId))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("삭제한 ID로 포스트 삭제 - 실패")
    @Test
    void deletePost_AlreadyDeletedId_ThrownException() {
        PostRequest createdPostRequest = new PostRequest(expectedWriting);
        PostResponse createdPostResponse = postService
            .createPost(authentication, createdPostRequest);

        postService.deletePost(authentication, createdPostResponse.getId());

        assertThatThrownBy(
            () -> postService.deletePost(authentication, createdPostResponse.getId()))
            .isInstanceOf(NotExistsException.class);
    }
}
