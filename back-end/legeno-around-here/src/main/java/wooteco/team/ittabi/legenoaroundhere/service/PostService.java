package wooteco.team.ittabi.legenoaroundhere.service;

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
}
