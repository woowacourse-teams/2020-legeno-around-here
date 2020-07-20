package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.team.ittabi.legenoaroundhere.domain.Post;
import wooteco.team.ittabi.legenoaroundhere.dto.PostRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    private PostService postService;
    private final Long postId = 1L;
    private final String expectedWriting = "Hello!!";

    @BeforeEach
    void setUp() {
        postService = new PostService(postRepository);
    }

    @DisplayName("포스트 생성 - 성공")
    @Test
    void createPost_SuccessToCreate() {
        PostRequest postRequest = new PostRequest(expectedWriting);
        Post post = new Post(expectedWriting);
        post.setId(postId);
        when(postRepository.save(any())).thenReturn(post);

        PostResponse postResponse = postService.createPost(postRequest);
        assertThat(postResponse.getId()).isEqualTo(postId);
        assertThat(postResponse.getWriting()).isEqualTo(expectedWriting);
    }

    @DisplayName("ID로 포스트 조회 - 성공, ID가 DB에 존재")
    @Test
    void findPost_HasId_SuccessToFind() {
        Post post = new Post(expectedWriting);
        post.setId(postId);
        when(postRepository.findById(any()))
            .thenReturn(Optional.of(post));

        PostResponse postResponse = postService.findPost(postId);
        assertThat(postResponse.getId()).isEqualTo(postId);
        assertThat(postResponse.getWriting()).isEqualTo(expectedWriting);
    }

    @DisplayName("ID로 포스트 조회 - 실패, ID가 DB에 없음")
    @Test
    void findPost_HasNotId_ThrownException() {
        when(postRepository.findById(any()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.findPost(postId))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("포스트 전체 목록 조회 - 성공")
    @Test
    void findAllPost_SuccessToFind() {
        when(postRepository.findAll()).thenReturn(Arrays.asList(new Post("hi"), new Post("hihi")));
        assertThat(postService.findAllPost()).hasSize(2);
    }

    @DisplayName("ID로 포스트 수정 - 실패, ID가 DB에 없음")
    @Test
    void updatePost_HasNotId_ThrownException() {
        PostRequest postRequest = new PostRequest(expectedWriting);
        when(postRepository.findById(any()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.updatePost(postId, postRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("ID로 포스트 삭제처리 - 실패, ID가 DB에 없음")
    @Test
    void deletePost_HasId_SuccessToDelete() {
        when(postRepository.findById(any()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.deletePost(postId))
            .isInstanceOf(IllegalArgumentException.class);
    }

}