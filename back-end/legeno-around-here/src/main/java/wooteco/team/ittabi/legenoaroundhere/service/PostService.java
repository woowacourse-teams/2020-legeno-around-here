package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.PostSearch;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.post.image.PostImage;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.PostAssembler;
import wooteco.team.ittabi.legenoaroundhere.dto.PostCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostImageResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostSearchRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostUpdateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostWithCommentsCountResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAuthorizedException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;
import wooteco.team.ittabi.legenoaroundhere.repository.AreaRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.PostImageRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.PostRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.SectorRepository;
import wooteco.team.ittabi.legenoaroundhere.utils.ImageUploader;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {

    private final NotificationService notificationService;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final AreaRepository areaRepository;
    private final SectorRepository sectorRepository;
    private final ImageUploader imageUploader;
    private final IAuthenticationFacade authenticationFacade;

    @Transactional
    public PostResponse createPost(@RequestBody PostCreateRequest postCreateRequest) {
        User user = (User) authenticationFacade.getPrincipal();

        Area area = areaRepository.findById(postCreateRequest.getAreaId())
            .orElseThrow(() -> new WrongUserInputException("입력하신 지역이 존재하지 않습니다."));

        Sector sector = sectorRepository.findById(postCreateRequest.getSectorId())
            .filter(Sector::isUsed)
            .orElseThrow(() -> new WrongUserInputException("입력하신 부문이 존재하지 않습니다."));
        List<PostImage> postImages = findAllPostImagesByIds(postCreateRequest);

        Post post = PostAssembler.assemble(postCreateRequest, postImages, area, sector, user);
        postImages.forEach(postImage -> postImage.setPost(post));

        Post savedPost = postRepository.save(post);
        return PostResponse.of(user, savedPost);
    }

    private List<PostImage> findAllPostImagesByIds(PostCreateRequest postCreateRequest) {
        final List<Long> imageIds = postCreateRequest.getImageIds();

        if (Objects.isNull(imageIds)) {
            return Collections.emptyList();
        }
        return postImageRepository.findAllById(imageIds);
    }

    @Transactional(readOnly = true)
    public PostResponse findPost(Long id) {
        User user = (User) authenticationFacade.getPrincipal();

        Post post = findPostBy(id);
        return PostResponse.of(user, post);
    }

    private Post findPostBy(Long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 POST가 없습니다."));
    }

    @Transactional(readOnly = true)
    public Page<PostWithCommentsCountResponse> searchPosts(Pageable pageable,
        PostSearchRequest postSearchFilter) {
        User user = (User) authenticationFacade.getPrincipal();

        Page<Post> posts = getPostByFilter(pageable, postSearchFilter.toPostSearch());

        return posts.map(post -> PostWithCommentsCountResponse.of(user, post));
    }

    private Page<Post> getPostByFilter(Pageable pageable, PostSearch postSearch) {
        if (postSearch.isNotExistsFilter()) {
            return postRepository.findAllBy(pageable);
        }

        if (postSearch.isAreaFilter()) {
            return postRepository.findAllByAreaId(pageable, postSearch.getAreaId());
        }
        if (postSearch.isSectorFilter()) {
            return postRepository.findAllBySectorIds(pageable, postSearch.getSectorIds());
        }
        return postRepository.findAllByAreaIdsAndSectorIds(pageable, postSearch.getAreaId(),
            postSearch.getSectorIds());
    }

    @Transactional
    public PostResponse updatePost(Long id, PostUpdateRequest postUpdateRequest) {
        User user = (User) authenticationFacade.getPrincipal();

        Post post = postRepository.findById(id)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 POST가 없습니다."));
        validateIsCreator(post);

        updatePostImages(postUpdateRequest, post);
        post.setWriting(postUpdateRequest.getWriting());

        return PostResponse.of(user, post);
    }

    private void updatePostImages(PostUpdateRequest postUpdateRequest, Post post) {
        List<PostImage> postImages = postImageRepository
            .findAllById(postUpdateRequest.getImageIds());
        post.removeNonExistentImagesFromPost(postUpdateRequest.getImageIds());
        post.addPostImages(postImages);
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = findPostBy(id);
        validateIsCreator(post);
        postRepository.delete(post);
    }

    private void validateIsCreator(Post post) {
        User user = (User) authenticationFacade.getPrincipal();

        if (user.isNotEquals(post.getCreator())) {
            throw new NotAuthorizedException("권한이 없습니다.");
        }
    }

    @Transactional
    public void pressZzang(Long postId) {
        User user = (User) authenticationFacade.getPrincipal();

        Post post = findPostBy(postId);
        post.pressZzang(user);

        if (post.hasZzangCreator(user)) {
            notificationService.notifyPostZzangNotification(post);
        }
    }

    @Transactional
    public List<PostImageResponse> uploadPostImages(List<MultipartFile> images) {
        List<PostImage> postImages = imageUploader.uploadPostImages(images);
        List<PostImage> savedPostImages = postImageRepository.saveAll(postImages);

        return PostImageResponse.listOf(savedPostImages);
    }

    @Transactional(readOnly = true)
    public Page<PostWithCommentsCountResponse> findMyPosts(Pageable pageable) {
        User user = (User) authenticationFacade.getPrincipal();

        Page<Post> posts = postRepository.findAllByCreator(pageable, user);

        return posts.map(post -> PostWithCommentsCountResponse.of(user, post));
    }

    @Transactional(readOnly = true)
    public Page<PostWithCommentsCountResponse> findPostsByUserId(Pageable pageable, Long userId) {
        User user = (User) authenticationFacade.getPrincipal();

        Page<Post> posts = postRepository.findAllByCreatorId(pageable, userId);

        return posts.map(post -> PostWithCommentsCountResponse.of(user, post));
    }
}
