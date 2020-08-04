package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.domain.Image;
import wooteco.team.ittabi.legenoaroundhere.domain.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.State;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.PostRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAuthorizedException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.PostRepository;

@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ImageService imageService;

    @Transactional
    public PostResponse createPost(PostRequest postRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postRequest.toPost(user);
        List<Image> images = uploadImages(postRequest);

        images.forEach(post::addImage);
        Post savedPost = postRepository.save(post);

        return PostResponse.of(savedPost);
    }

    private List<Image> uploadImages(PostRequest postRequest) {
        if (postRequest.isImagesNull()) {
            return Collections.emptyList();
        }

        return postRequest.getImages().stream()
            .map(imageService::upload)
            .collect(Collectors.toList());
    }

    public PostResponse findPost(Long id) {
        Post post = findNotDeletedPost(id);
        return PostResponse.of(post);
    }

    public Post findNotDeletedPost(Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 POST가 없습니다."));
        if (post.isSameState(State.DELETED)) {
            log.debug("이미 삭제된 POST, id = {}", id);
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
    public void updatePost(Long id, PostRequest postRequest) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 POST가 없습니다."));
        validateIsOwner(post);
        post.setWriting(postRequest.getWriting());
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = findNotDeletedPost(id);
        validateIsOwner(post);
        post.setState(State.DELETED);
    }

    private void validateIsOwner(Post post) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.isNotSame(post.getUser())) {
            throw new NotAuthorizedException("권한이 없습니다.");
        }
    }
}
