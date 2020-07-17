package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.team.ittabi.legenoaroundhere.domain.Post;
import wooteco.team.ittabi.legenoaroundhere.dto.PostRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponse createPost(PostRequest postRequest) {
        Post post = postRepository.save(postRequest.toPost());
        return PostResponse.of(post);
    }

    public PostResponse findPost(Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 POST가 없습니다."));
        return PostResponse.of(post);
    }

    public List<PostResponse> findAllPost() {
        return PostResponse.listOf(postRepository.findAll());
    }

    public void updatePost(Long id, PostRequest postRequest) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 POST가 없습니다."));
        post.setWriting(postRequest.getWriting());
    }
}
