package wooteco.team.ittabi.legenoaroundhere.service;

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
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.post.State;
import wooteco.team.ittabi.legenoaroundhere.domain.post.image.Image;
import wooteco.team.ittabi.legenoaroundhere.domain.post.zzang.PostZzang;
import wooteco.team.ittabi.legenoaroundhere.domain.post.zzang.ZzangState;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.PostCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostUpdateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostWithCommentsCountResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAuthorizedException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;
import wooteco.team.ittabi.legenoaroundhere.repository.AreaRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.CommentRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.PostRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.SectorRepository;
import wooteco.team.ittabi.legenoaroundhere.utils.ImageUploader;

@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final AreaRepository areaRepository;
    private final SectorRepository sectorRepository;
    private final ImageUploader imageUploader;
    private final IAuthenticationFacade authenticationFacade;

    @Transactional
    public PostResponse createPost(PostCreateRequest postCreateRequest) {
        User user = (User) authenticationFacade.getPrincipal();

        Area area = areaRepository.findById(postCreateRequest.getAreaId())
            .filter(Area::isUsed)
            .orElseThrow(() -> new WrongUserInputException("입력하신 지역이 존재하지 않습니다."));

        Sector sector = sectorRepository.findById(postCreateRequest.getSectorId())
            .filter(Sector::isUsed)
            .orElseThrow(() -> new WrongUserInputException("입력하신 부문이 존재하지 않습니다."));

        Post post = postCreateRequest.toPost(area, sector, user);
        uploadImages(postCreateRequest).forEach(image -> image.setPost(post));

        Post savedPost = postRepository.save(post);
        List<Comment> comments = findAllComment(post.getId());
        ZzangState zzangState = findPostZzang(post).getZzangState();
        return PostResponse.of(savedPost, comments, zzangState);
    }

    private List<Image> uploadImages(PostCreateRequest postCreateRequest) {
        if (postCreateRequest.isImagesNull()) {
            return Collections.emptyList();
        }
        return postCreateRequest.getImages().stream()
            .map(imageUploader::uploadImage)
            .collect(Collectors.toList());
    }

    public PostResponse findPost(Long id) {
        Post post = findNotDeletedPost(id);
        List<Comment> comments = findAllComment(post.getId());
        ZzangState zzangState = findPostZzang(post).getZzangState();
        return PostResponse.of(post, comments, zzangState);
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
                ZzangState zzangState = findPostZzang(post).getZzangState();
                return PostWithCommentsCountResponse.of(post, comments, zzangState);
            });
    }

    @Transactional
    public void updatePost(Long id, PostUpdateRequest postUpdateRequest) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 POST가 없습니다."));
        validateIsCreator(post);
        post.setWriting(postUpdateRequest.getWriting());
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = findNotDeletedPost(id);
        validateIsCreator(post);
        post.setState(State.DELETED);
    }

    private List<Comment> findAllComment(Long postId) {
        return commentRepository.findAllByPostIdAndStateNot(postId, State.DELETED);
    }

    private PostZzang findPostZzang(Post post) {
        User user = (User) authenticationFacade.getPrincipal();
        return post.findPostZzangBy(user);
    }

    private void validateIsCreator(Post post) {
        User user = (User) authenticationFacade.getPrincipal();

        if (user.isNotSame(post.getCreator())) {
            throw new NotAuthorizedException("권한이 없습니다.");
        }
    }
}
