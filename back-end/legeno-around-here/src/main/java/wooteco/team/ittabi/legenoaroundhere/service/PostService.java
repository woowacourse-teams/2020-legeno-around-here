package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.domain.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.State;
import wooteco.team.ittabi.legenoaroundhere.dto.PostRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.PostRepository;

@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponse createPost(Authentication authentication, PostRequest postRequest) {
        Post post = postRepository.save(postRequest.toPost());
        return PostResponse.of(post);
    }

    public PostResponse findPost(Authentication authentication, Long id) {
        Post post = findNotDeletedPost(id);
        return PostResponse.of(post);
    }

    private Post findNotDeletedPost(Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 POST가 없습니다."));
        if (post.isSameState(State.DELETED)) {
            throw new NotExistsException("삭제된 POST 입니다!");
        }
        return post;
    }

    public List<PostResponse> findAllPost(Authentication authentication) {
        List<Post> posts = postRepository.findAll().stream()
            .filter(post -> post.isNotSameState(State.DELETED))
            .collect(Collectors.toList());
        return PostResponse.listOf(posts);
    }

    public void updatePost(Authentication authentication, Long id, PostRequest postRequest) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 POST가 없습니다."));
        post.setWriting(postRequest.getWriting());
    }

    public void deletePost(Authentication authentication, Long id) {
        Post post = findNotDeletedPost(id);
        post.setState(State.DELETED);
    }
}
