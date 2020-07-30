package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import wooteco.team.ittabi.legenoaroundhere.dto.PostRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;

@DataJpaTest
@Import(PostService.class)
public class PostServiceTest {

    @Autowired
    private PostService postService;

    private final String expectedWriting = "Hello!!";

    @DisplayName("포스트 생성 - 성공")
    @Test
    void createPost_SuccessToCreate() {
        PostRequest postRequest = new PostRequest(expectedWriting);

        PostResponse postResponse = postService.createPost(postRequest);

        assertThat(postResponse.getId()).isNotNull();
        assertThat(postResponse.getWriting()).isEqualTo(expectedWriting);
    }

    @DisplayName("ID로 포스트 조회 - 성공")
    @Test
    void findPost_HasId_SuccessToFind() {
        PostRequest postRequest = new PostRequest(expectedWriting);
        PostResponse createdPostResponse = postService.createPost(postRequest);

        PostResponse postResponse = postService.findPost(createdPostResponse.getId());

        assertThat(postResponse.getId()).isEqualTo(createdPostResponse.getId());
        assertThat(postResponse.getWriting()).isEqualTo(createdPostResponse.getWriting());
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
        PostResponse createdPostResponse = postService.createPost(createdPostRequest);
        PostRequest updatedPostRequest = new PostRequest(updatedPostWriting);

        postService.updatePost(createdPostResponse.getId(), updatedPostRequest);
        PostResponse updatedPostResponse = postService.findPost(createdPostResponse.getId());

        assertThat(updatedPostResponse.getWriting()).isEqualTo(updatedPostWriting);
    }

    @DisplayName("ID로 포스트 수정 - 실패")
    @Test
    void updatePost_HasNotId_ThrownException() {
        PostRequest postRequest = new PostRequest(expectedWriting);
        Long invalidId = -1L;

        assertThatThrownBy(() -> postService.updatePost(invalidId, postRequest))
            .isInstanceOf(NotExistsException.class);
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

        assertThatThrownBy(() -> postService.deletePost(createdPostResponse.getId()))
            .isInstanceOf(NotExistsException.class);
    }
}
