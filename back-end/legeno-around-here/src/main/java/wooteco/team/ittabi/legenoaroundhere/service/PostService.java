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
import wooteco.team.ittabi.legenoaroundhere.domain.Image;
import wooteco.team.ittabi.legenoaroundhere.domain.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.PostSearch;
import wooteco.team.ittabi.legenoaroundhere.domain.State;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostSearchRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostUpdateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostWithCommentsCountResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAuthorizedException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;
import wooteco.team.ittabi.legenoaroundhere.repository.AreaRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.PostRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.SectorRepository;

@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class PostService {

    private static final State Deleted = State.DELETED;

    private final PostRepository postRepository;
    private final AreaRepository areaRepository;
    private final SectorRepository sectorRepository;
    private final CommentService commentService;
    private final ImageService imageService;
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

        List<Image> images = uploadImages(postCreateRequest);
        images.forEach(image -> image.setPost(post));

        Post savedPost = postRepository.save(post);
        List<CommentResponse> commentResponses = commentService.findAllComment(savedPost.getId());

        return PostResponse.of(savedPost, commentResponses);
    }

    private List<Image> uploadImages(PostCreateRequest postCreateRequest) {
        if (postCreateRequest.isImagesNull()) {
            return Collections.emptyList();
        }

        return postCreateRequest.getImages().stream()
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

    public Page<PostWithCommentsCountResponse> searchAllPost(Pageable pageable,
        PostSearchRequest postSearchFilter) {
        Page<Post> posts = getPostByFilter(pageable, postSearchFilter.toPostSearch());

        return posts.map(post -> PostWithCommentsCountResponse
            .of(post, commentService.findAllComment(post.getId())));
    }

    private Page<Post> getPostByFilter(Pageable pageable, PostSearch postSearch) {
        if (postSearch.isNoFilter()) {
            return postRepository.findByStateNot(pageable, Deleted);
        }

        if (postSearch.isAreaFilter()) {
            List<Area> areas = findAllAreas(postSearch.getAreaIds());
            return postRepository.findAllByStateNotAndAreaIn(pageable, Deleted, areas);
        }

        if (postSearch.isSectorFilter()) {
            List<Sector> sectors = findAllSectors(postSearch.getSectorIds());
            return postRepository.findAllByStateNotAndSectorIn(pageable, Deleted, sectors);
        }

        List<Area> areas = findAllAreas(postSearch.getAreaIds());
        List<Sector> sectors = findAllSectors(postSearch.getSectorIds());
        return postRepository
            .findAllByStateNotAndAreaInAndSectorIn(pageable, Deleted, areas, sectors);
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
        Post post = findNotDeletedPost(id);
        validateIsCreator(post);
        post.setState(State.DELETED);
    }

    private void validateIsCreator(Post post) {
        User user = (User) authenticationFacade.getPrincipal();

        if (user.isNotSame(post.getCreator())) {
            throw new NotAuthorizedException("권한이 없습니다.");
        }
    }
}
