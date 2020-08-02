package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.domain.Image;
import wooteco.team.ittabi.legenoaroundhere.domain.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.State;
import wooteco.team.ittabi.legenoaroundhere.dto.PostRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.PostRepository;

@Slf4j
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
        Post post = postRequest.toPost();
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

    private Post findNotDeletedPost(Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 POST가 없습니다."));
        if (post.isSameState(State.DELETED)) {
            log.debug("이미 삭제된 POST, id = {}", id);
            throw new NotExistsException("삭제된 POST 입니다!");
        }
        return post;
    }

    public List<PostResponse> findAllPost() {
        return postRepository.findAll().stream()
            .filter(post -> post.isNotSameState(State.DELETED))
            .map(PostResponse::of)
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
