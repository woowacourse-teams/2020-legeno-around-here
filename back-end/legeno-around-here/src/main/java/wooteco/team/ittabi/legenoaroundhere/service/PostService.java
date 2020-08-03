package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.domain.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.State;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.PostRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAuthorizedException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.PostRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.UserRepository;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostResponse createPost(Authentication authentication, PostRequest postRequest) {
        User user = (User) authentication.getPrincipal();
        Post post = postRepository.save(postRequest.toPost(user));
        return PostResponse.of(post);
    }

    public PostResponse findPost(Long id) {
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

    public List<PostResponse> findAllPost() {
        List<Post> posts = postRepository.findAll().stream()
            .filter(post -> post.isNotSameState(State.DELETED))
            .collect(Collectors.toList());
        return PostResponse.listOf(posts);
    }

    @Transactional
    public void updatePost(Authentication authentication, Long id, PostRequest postRequest) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 POST가 없습니다."));
        validateIsOwner(authentication, post);
        post.setWriting(postRequest.getWriting());
    }

    @Transactional
    public void deletePost(Authentication authentication, Long id) {
        Post post = findNotDeletedPost(id);
        validateIsOwner(authentication, post);
        post.setState(State.DELETED);
    }

    private void validateIsOwner(Authentication authentication, Post post) {
        User user = (User) authentication.getPrincipal();

        if (user.isNotSame(post.getUser())) {
            throw new NotAuthorizedException("권한이 없습니다.");
        }
    }
}
