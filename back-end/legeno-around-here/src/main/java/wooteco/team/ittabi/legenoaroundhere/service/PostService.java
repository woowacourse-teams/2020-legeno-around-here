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
import wooteco.team.ittabi.legenoaroundhere.domain.PostSearch;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.post.image.PostImage;
import wooteco.team.ittabi.legenoaroundhere.domain.post.zzang.PostZzang;
import wooteco.team.ittabi.legenoaroundhere.domain.post.zzang.ZzangState;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.PostCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostSearchRequest;
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

    private List<PostImage> uploadImages(PostCreateRequest postCreateRequest) {
        if (postCreateRequest.isImagesNull()) {
            return Collections.emptyList();
        }
        return postCreateRequest.getImages().stream()
            .map(imageUploader::uploadImage)
            .collect(Collectors.toList());
    }

    public PostResponse findPost(Long id) {
        Post post = findPostBy(id);
        List<Comment> comments = findAllComment(post.getId());
        ZzangState zzangState = findPostZzang(post).getZzangState();
        return PostResponse.of(post, comments, zzangState);
    }

    private Post findPostBy(Long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 POST가 없습니다."));
    }

    public Page<PostWithCommentsCountResponse> searchAllPost(Pageable pageable,
        PostSearchRequest postSearchFilter) {
        Page<Post> posts = getPostByFilter(pageable, postSearchFilter.toPostSearch());

        return posts
            .map(post -> {
                List<Comment> comments = findAllComment(post.getId());
                ZzangState zzangState = findPostZzang(post).getZzangState();
                return PostWithCommentsCountResponse.of(post, comments, zzangState);
            });
    }

    private Page<Post> getPostByFilter(Pageable pageable, PostSearch postSearch) {
        if (postSearch.isNotExistsFilter()) {
            return postRepository.findAllBy(pageable);
        }

        if (postSearch.isAreaFilter()) {
            List<Area> areas = findAllAreas(postSearch.getAreaIds());
            return postRepository.findAllByAreaIn(pageable, areas);
        }

        if (postSearch.isSectorFilter()) {
            List<Sector> sectors = findAllSectors(postSearch.getSectorIds());
            return postRepository.findAllBySectorIn(pageable, sectors);
        }

        List<Area> areas = findAllAreas(postSearch.getAreaIds());
        List<Sector> sectors = findAllSectors(postSearch.getSectorIds());
        return postRepository.findAllByAreaInAndSectorIn(pageable, areas, sectors);
    }

    private List<Area> findAllAreas(List<Long> areaIds) {
        List<Area> areas = areaRepository.findAllById(areaIds);
        if (areas.size() != areaIds.size()) {
            throw new WrongUserInputException("유효하지 않은 Area ID를 사용하셨습니다.");
        }
        return areas;
    }

    private List<Sector> findAllSectors(List<Long> sectorIds) {
        List<Sector> sectors = sectorRepository.findAllById(sectorIds);
        if (sectors.size() != sectorIds.size()) {
            throw new WrongUserInputException("유효하지 않은 Sector ID를 사용하셨습니다.");
        }
        return sectors;
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
        Post post = findPostBy(id);
        validateIsCreator(post);
        postRepository.delete(post);
    }

    private List<Comment> findAllComment(Long postId) {
        return commentRepository.findAllByPostId(postId);
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
