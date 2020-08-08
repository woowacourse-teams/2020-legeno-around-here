package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.Image;
import wooteco.team.ittabi.legenoaroundhere.domain.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.State;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentResponse;
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

    private static final State Deleted = State.DELETED;

    private final PostRepository postRepository;
    private final CommentService commentService;
    private final ImageService imageService;
    private final IAuthenticationFacade authenticationFacade;

    @Transactional
    public PostResponse createPost(PostRequest postRequest) {
        User user = (User) authenticationFacade.getPrincipal();
        Post post = postRequest.toPost(user);
        List<Image> images = uploadImages(postRequest);

        images.forEach(image -> image.setPost(post));
        Post savedPost = postRepository.save(post);
        List<CommentResponse> commentResponses = commentService.findAllComment(savedPost.getId());

        return PostResponse.of(savedPost, commentResponses);
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
        List<CommentResponse> commentResponses = commentService.findAllComment(post.getId());
        return PostResponse.of(post, commentResponses);
    }

    public Post findNotDeletedPost(Long id) {
        return postRepository.findByIdAndStateNot(id, Deleted)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 POST가 없습니다."));
    }

    public List<PostResponse> findAllPost() {
        List<Post> posts = new ArrayList<>(postRepository.findByStateNot(Deleted));
        return posts.stream()
            .map(post -> PostResponse.of(post, commentService.findAllComment(post.getId())))
            .collect(Collectors.toList());
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
        User user = (User) authenticationFacade.getPrincipal();

        if (user.isNotSame(post.getUser())) {
            throw new NotAuthorizedException("권한이 없습니다.");
        }
    }
}
