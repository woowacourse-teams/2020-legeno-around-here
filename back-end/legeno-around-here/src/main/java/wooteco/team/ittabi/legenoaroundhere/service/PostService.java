package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.post.State;
import wooteco.team.ittabi.legenoaroundhere.domain.post.image.Image;
import wooteco.team.ittabi.legenoaroundhere.domain.post.like.Like;
import wooteco.team.ittabi.legenoaroundhere.domain.post.like.LikeState;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.PostRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostWithCommentsCountResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAuthorizedException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.CommentRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.LikeRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.PostRepository;
import wooteco.team.ittabi.legenoaroundhere.utils.ImageUploader;

@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final ImageUploader imageUploader;
    private final IAuthenticationFacade authenticationFacade;

    @Transactional
    public PostResponse createPost(PostRequest postRequest) {
        User user = (User) authenticationFacade.getPrincipal();
        Post post = postRequest.toPost(user);
        uploadImages(postRequest).forEach(image -> image.setPost(post));

        Post savedPost = postRepository.save(post);
        List<Comment> comments = findAllComment(post.getId());
        LikeState likeState = findLike(post).getLikeState();
        return PostResponse.of(savedPost, comments, likeState);
    }

    private List<Image> uploadImages(PostRequest postRequest) {
        if (postRequest.isImagesNull()) {
            return Collections.emptyList();
        }
        return postRequest.getImages().stream()
            .map(imageUploader::uploadImage)
            .collect(Collectors.toList());
    }

    public PostResponse findPost(Long id) {
        Post post = findNotDeletedPost(id);
        List<Comment> comments = findAllComment(post.getId());
        LikeState likeState = findLike(post).getLikeState();
        return PostResponse.of(post, comments, likeState);
    }

    private Post findNotDeletedPost(Long id) {
        return postRepository.findByIdAndStateNot(id, State.DELETED)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 POST가 없습니다."));
    }

    public Page<PostWithCommentsCountResponse> findAllPost(Pageable pageable) {
        Page<Post> posts = postRepository.findByStateNot(pageable, State.DELETED);
        return posts
            .map(post -> {
                List<Comment> comments = findAllComment(post.getId());
                LikeState likeState = findLike(post).getLikeState();
                return PostWithCommentsCountResponse.of(post, comments, likeState);
            });
    }

    @Transactional
    public void updatePost(Long id, PostRequest postRequest) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 POST가 없습니다."));
        validateIsCreator(post);
        post.setWriting(postRequest.getWriting());
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = findNotDeletedPost(id);
        validateIsCreator(post);
        post.setState(State.DELETED);
    }

    private List<Comment> findAllComment(Long postId) {
        return new ArrayList<>(commentRepository.findAllByPostIdAndStateNot(postId, State.DELETED));
    }

    private Like findLike(Post post) {
        User user = (User) authenticationFacade.getPrincipal();
        return likeRepository.findByPostIdAndCreatorId(post.getId(), user.getId())
            .orElseGet(() -> new Like(post, user));
    }

    private void validateIsCreator(Post post) {
        User user = (User) authenticationFacade.getPrincipal();

        if (user.isNotSame(post.getCreator())) {
            throw new NotAuthorizedException("권한이 없습니다.");
        }
    }
}
