package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
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
    private Long postId = 1L;

    @BeforeEach
    void setUp() {
        postService = new PostService(postRepository);
    }

    @Test
    void createPost() {
        String expectedWriting = "Hello!!";
        PostRequest postRequest = new PostRequest(expectedWriting);
        when(postRepository.save(any())).thenReturn(new Post(postId, expectedWriting));

        PostResponse postResponse = postService.createPost(postRequest);
        assertThat(postResponse.getId()).isEqualTo(postId);
        assertThat(postResponse.getWriting()).isEqualTo(expectedWriting);
    }
}
