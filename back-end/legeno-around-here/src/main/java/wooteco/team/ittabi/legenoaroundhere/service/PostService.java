package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.domain.Image;
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
    private final ImageService imageService;

    public PostService(PostRepository postRepository, ImageService imageService) {
        this.postRepository = postRepository;
        this.imageService = imageService;
    }

    public PostResponse createPost(PostRequest postRequest) {
        Post post = postRepository.save(postRequest.toPost());
        List<Image> images = saveImages(postRequest, post);
        return PostResponse.of(post, images);
    }

    private List<Image> saveImages(PostRequest postRequest, Post post) {
        if (postRequest.isImagesNull()) {
            return Collections.emptyList();
        }
        return postRequest.getImages().stream()
            .map(multipartFile -> imageService.save(multipartFile, post))
            .collect(Collectors.toList());
    }

    public PostResponse findPost(Long id) {
        Post post = findNotDeletedPost(id);
        List<Image> images = imageService.findAllByPostId(id);
        return PostResponse.of(post, images);
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
        return postRepository.findAll().stream()
            .filter(post -> post.isNotSameState(State.DELETED))
            .map(post -> PostResponse.of(post, imageService.findAllByPostId(post.getId())))
            .collect(Collectors.toList());
    }

    public void updatePost(Long id, PostRequest postRequest) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 POST가 없습니다."));
        post.setWriting(postRequest.getWriting());
    }

    public void deletePost(Long id) {
        Post post = findNotDeletedPost(id);
        post.setState(State.DELETED);
    }
}
