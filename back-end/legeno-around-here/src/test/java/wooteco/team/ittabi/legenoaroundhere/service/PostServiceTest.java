package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.domain.post.RankingCriteria.LAST_MONTH;
import static wooteco.team.ittabi.legenoaroundhere.domain.post.RankingCriteria.LAST_WEEK;
import static wooteco.team.ittabi.legenoaroundhere.domain.post.RankingCriteria.TOTAL;
import static wooteco.team.ittabi.legenoaroundhere.domain.post.RankingCriteria.YESTERDAY;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_OTHER_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_CONTENT_TYPE;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_EMPTY_MULTIPART_FILES;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_POST_INVALID_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_POST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_ANOTHER_REQUEST;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_REQUEST;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_OTHER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.PostCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostSearchRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostUpdateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostWithCommentsCountResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostZzangResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.RankingRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAuthorizedException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.PostRepository;
import wooteco.team.ittabi.legenoaroundhere.utils.FileConverter;

public class PostServiceTest extends ServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SectorService sectorService;

    private User user;
    private User userA;
    private User userB;
    private User userC;
    private User userD;
    private User userE;
    private User another;
    private SectorResponse sector;
    private Long sectorId;
    private Long sectorOtherId;

    @BeforeEach
    void setUp() {
        user = createUser(TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        another = createUser(TEST_USER_OTHER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        userA = createUser(TEST_USER_EMAIL + "A", TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        userB = createUser(TEST_USER_EMAIL + "B", TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        userC = createUser(TEST_USER_EMAIL + "C", TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        userD = createUser(TEST_USER_EMAIL + "D", TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        userE = createUser(TEST_USER_EMAIL + "E", TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        setAuthentication(user);

        sector = sectorService.createSector(TEST_SECTOR_REQUEST);
        sectorId = sector.getId();
        sectorOtherId = sectorService.createSector(TEST_SECTOR_ANOTHER_REQUEST).getId();
    }

    @DisplayName("이미지를 포함하지 않는 포스트 생성 - 성공")
    @Test
    void createPostWithoutImage_SuccessToCreate() {
        PostCreateRequest postCreateRequest
            = new PostCreateRequest(TEST_POST_WRITING, TEST_IMAGE_EMPTY_MULTIPART_FILES,
            TEST_AREA_ID, sectorId);

        PostResponse postResponse = postService.createPost(postCreateRequest);

        assertThat(postResponse.getId()).isNotNull();
        assertThat(postResponse.getWriting()).isEqualTo(TEST_POST_WRITING);
        assertThat(postResponse.getCreator()).isEqualTo(UserResponse.from(user));
        assertThat(postResponse.getSector()).isEqualTo(sector);
    }

    @DisplayName("이미지를 포함한 포스트 생성 - 성공")
    @Test
    void createPostWithImage_SuccessToCreate() throws IOException {
        MultipartFile multipartFile
            = FileConverter.convert("image1.jpg", TEST_IMAGE_CONTENT_TYPE);
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            Collections.singletonList(multipartFile), TEST_AREA_ID, sectorId);

        PostResponse postResponse = postService.createPost(postCreateRequest);

        assertThat(postResponse.getWriting()).isEqualTo(TEST_POST_WRITING);
        assertThat(postResponse.getImages()).hasSize(1);
        assertThat(postResponse.getCreator()).isEqualTo(UserResponse.from(user));
        assertThat(postResponse.getSector()).isEqualTo(sector);
    }

    @DisplayName("ID로 포스트 조회 - 성공")
    @Test
    void findPost_HasId_SuccessToFind() {
        PostCreateRequest postCreateRequest
            = new PostCreateRequest(TEST_POST_WRITING, TEST_IMAGE_EMPTY_MULTIPART_FILES,
            TEST_AREA_ID, sectorId);
        PostResponse createdPostResponse = postService.createPost(postCreateRequest);

        PostResponse postResponse = postService.findPost(createdPostResponse.getId());

        assertThat(postResponse.getId()).isEqualTo(createdPostResponse.getId());
        assertThat(postResponse.getWriting()).isEqualTo(createdPostResponse.getWriting());
        assertThat(postResponse.getCreator()).isEqualTo(UserResponse.from(user));
        assertThat(postResponse.getSector()).isEqualTo(sector);
    }

    @DisplayName("ID로 포스트 조회 - 실패, 이미 지워진 포스트")
    @Test
    void findPost_AlreadyDeletedPost_ThrownException() {
        PostCreateRequest postCreateRequest
            = new PostCreateRequest(TEST_POST_WRITING, TEST_IMAGE_EMPTY_MULTIPART_FILES,
            TEST_AREA_ID, sectorId);
        PostResponse createdPostResponse = postService.createPost(postCreateRequest);
        postService.deletePost(createdPostResponse.getId());

        assertThatThrownBy(() -> postService.findPost(createdPostResponse.getId()))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("ID로 포스트 조회 - 실패, 유효하지 않은 포스트 ID")
    @Test
    void findPost_HasNotId_ThrownException() {
        assertThatThrownBy(() -> postService.findPost(TEST_POST_INVALID_ID))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("포스트 전체 목록 검색 - 성공")
    @Test
    void searchAllPost_NoFilter_SuccessToFind() {
        PostCreateRequest postCreateRequest
            = new PostCreateRequest(TEST_POST_WRITING, TEST_IMAGE_EMPTY_MULTIPART_FILES,
            TEST_AREA_ID, sectorId);
        postService.createPost(postCreateRequest);
        postService.createPost(postCreateRequest);

        PostSearchRequest postSearchRequest = new PostSearchRequest(null, null);
        Page<PostWithCommentsCountResponse> posts
            = postService.searchAllPost(Pageable.unpaged(), postSearchRequest);

        assertThat(posts.getContent()).hasSize(2);
    }

    @DisplayName("포스트 전체 목록 검색(Area Filter) 단 건 - 성공")
    @Test
    void searchAllPost_AreaFilter_SuccessToFind() {
        PostCreateRequest postCreateRequest
            = new PostCreateRequest(TEST_POST_WRITING, TEST_IMAGE_EMPTY_MULTIPART_FILES,
            TEST_AREA_ID, sectorId);
        postService.createPost(postCreateRequest);
        postService.createPost(postCreateRequest);

        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES,
            TEST_AREA_OTHER_ID, sectorOtherId);
        postService.createPost(postCreateRequest);

        PostSearchRequest postSearchRequest = new PostSearchRequest(
            String.valueOf(TEST_AREA_OTHER_ID), null);
        Page<PostWithCommentsCountResponse> posts
            = postService.searchAllPost(Pageable.unpaged(), postSearchRequest);

        assertThat(posts.getContent()).hasSize(1);
    }

    @DisplayName("포스트 전체 목록 검색(Sector Filter) - 성공")
    @Test
    void searchAllPost_SectorFilter_SuccessToFind() {
        PostCreateRequest postCreateRequest
            = new PostCreateRequest(TEST_POST_WRITING, TEST_IMAGE_EMPTY_MULTIPART_FILES,
            TEST_AREA_ID, sectorId);
        postService.createPost(postCreateRequest);
        postService.createPost(postCreateRequest);

        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES,
            TEST_AREA_ID, sectorOtherId);
        postService.createPost(postCreateRequest);
        postService.createPost(postCreateRequest);

        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES,
            TEST_AREA_OTHER_ID, sectorId);
        postService.createPost(postCreateRequest);
        postService.createPost(postCreateRequest);

        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES,
            TEST_AREA_OTHER_ID, sectorOtherId);
        postService.createPost(postCreateRequest);

        PostSearchRequest postSearchRequest = new PostSearchRequest(
            String.valueOf(TEST_AREA_OTHER_ID), String.valueOf(sectorOtherId));
        Page<PostWithCommentsCountResponse> posts
            = postService.searchAllPost(Pageable.unpaged(), postSearchRequest);

        assertThat(posts.getContent()).hasSize(1);
    }

    @DisplayName("포스트 전체 목록 검색(Area + Sector Filter) - 성공")
    @Test
    void searchAllPost_AreaAndSectorFilter_SuccessToFind() {
        PostCreateRequest postCreateRequest
            = new PostCreateRequest(TEST_POST_WRITING, TEST_IMAGE_EMPTY_MULTIPART_FILES,
            TEST_AREA_ID, sectorId);
        postService.createPost(postCreateRequest);
        postService.createPost(postCreateRequest);

        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES,
            TEST_AREA_OTHER_ID, sectorOtherId);
        postService.createPost(postCreateRequest);

        PostSearchRequest postSearchRequest = new PostSearchRequest(
            null, String.valueOf(sectorOtherId));
        Page<PostWithCommentsCountResponse> posts
            = postService.searchAllPost(Pageable.unpaged(), postSearchRequest);

        assertThat(posts.getContent()).hasSize(1);
    }

    @DisplayName("ID로 포스트 수정 - 성공")
    @Test
    void updatePost_HasId_SuccessToUpdate() {
        String updatedPostWriting = "Jamie and BingBong";
        PostCreateRequest createdPostCreateRequest
            = new PostCreateRequest(TEST_POST_WRITING, TEST_IMAGE_EMPTY_MULTIPART_FILES,
            TEST_AREA_ID, sectorId);
        PostResponse createdPostResponse = postService.createPost(createdPostCreateRequest);
        PostUpdateRequest postUpdateRequest
            = new PostUpdateRequest(updatedPostWriting, TEST_IMAGE_EMPTY_MULTIPART_FILES);

        postService.updatePost(createdPostResponse.getId(), postUpdateRequest);
        PostResponse updatedPostResponse = postService.findPost(createdPostResponse.getId());

        assertThat(updatedPostResponse.getWriting()).isEqualTo(updatedPostWriting);
        assertThat(updatedPostResponse.getCreator()).isEqualTo(UserResponse.from(user));
    }

    @DisplayName("ID로 포스트 수정 - 실패")
    @Test
    void updatePost_HasNotId_ThrownException() {
        PostUpdateRequest postUpdateRequest
            = new PostUpdateRequest(TEST_POST_WRITING, TEST_IMAGE_EMPTY_MULTIPART_FILES);

        assertThatThrownBy(() -> postService.updatePost(TEST_POST_INVALID_ID, postUpdateRequest))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("ID로 포스트 수정 - 예외 발생, 작성자가 아님")
    @Test
    void updatePost_IfNotCreator_ThrowException() {
        String updatedPostWriting = "Jamie and BingBong";
        PostCreateRequest createdPostCreateRequest
            = new PostCreateRequest(TEST_POST_WRITING, TEST_IMAGE_EMPTY_MULTIPART_FILES,
            TEST_AREA_ID, sectorId);
        PostResponse createdPostResponse = postService.createPost(createdPostCreateRequest);
        PostUpdateRequest postUpdateRequest
            = new PostUpdateRequest(updatedPostWriting, TEST_IMAGE_EMPTY_MULTIPART_FILES);

        setAuthentication(another);
        assertThatThrownBy(() -> postService
            .updatePost(createdPostResponse.getId(), postUpdateRequest))
            .isInstanceOf(NotAuthorizedException.class);
    }

    @DisplayName("ID로 포스트 삭제 - 성공")
    @Test
    void deletePost_HasId_SuccessToDelete() {
        PostCreateRequest createdPostCreateRequest
            = new PostCreateRequest(TEST_POST_WRITING, TEST_IMAGE_EMPTY_MULTIPART_FILES,
            TEST_AREA_ID, sectorId);
        PostResponse createdPostResponse = postService.createPost(createdPostCreateRequest);

        postRepository.deleteById(createdPostResponse.getId());

        assertThatThrownBy(() -> postService.findPost(createdPostResponse.getId()))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("ID로 포스트 삭제 - 실패")
    @Test
    void deletePost_HasNotId_ThrownException() {
        assertThatThrownBy(() -> postService.deletePost(TEST_POST_INVALID_ID))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("삭제한 ID로 포스트 삭제 - 실패")
    @Test
    void deletePost_AlreadyDeletedId_ThrownException() {
        PostCreateRequest createdPostCreateRequest
            = new PostCreateRequest(TEST_POST_WRITING, TEST_IMAGE_EMPTY_MULTIPART_FILES,
            TEST_AREA_ID, sectorId);
        PostResponse createdPostResponse = postService.createPost(createdPostCreateRequest);

        postService.deletePost(createdPostResponse.getId());

        assertThatThrownBy(() -> postService.deletePost(createdPostResponse.getId()))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("ID로 포스트 삭제 - 예외 발생, 작성자가 아님")
    @Test
    void deletePost_IfNotCreator_ThrowException() {
        PostCreateRequest createdPostCreateRequest
            = new PostCreateRequest(TEST_POST_WRITING, TEST_IMAGE_EMPTY_MULTIPART_FILES,
            TEST_AREA_ID, sectorId);
        PostResponse createdPostResponse = postService.createPost(createdPostCreateRequest);

        setAuthentication(another);
        assertThatThrownBy(
            () -> postService.deletePost(createdPostResponse.getId()))
            .isInstanceOf(NotAuthorizedException.class);
    }

    @DisplayName("비활성화된 좋아요를 활성화")
    @Test
    void pressPostZzang_activePostZzang_SuccessToActivePostZzang() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        Long postId = postService.createPost(postCreateRequest).getId();

        postService.pressZzang(postId);

        PostResponse post = postService.findPost(postId);
        PostZzangResponse zzang = post.getZzang();

        assertThat(zzang.getCount()).isEqualTo(1L);
        assertThat(zzang.isActivated()).isTrue();
    }

    @DisplayName("활성화된 좋아요를 비활성화")
    @Test
    void pressPostZzang_inactivePostZzang_SuccessToInactivePostZzang() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        Long postId = postService.createPost(postCreateRequest).getId();

        postService.pressZzang(postId);
        postService.pressZzang(postId);

        PostResponse post = postService.findPost(postId);
        PostZzangResponse zzang = post.getZzang();

        assertThat(zzang.getCount()).isEqualTo(0L);
        assertThat(zzang.isActivated()).isFalse();
    }

    @DisplayName("모든 부문, 모든 지역 랭킹 조회 - 전체 기간")
    @Test
    void searchRanking_AllSectorAllAreaTotalCriteria_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorOtherId);
        Long postIdD = postService.createPost(postCreateRequest).getId();

        //PostA 짱이야2
        //PostB 짱이야4
        //PostC 짱이야3
        //PostD 짱이야1
        //랭킹 순서: B -> C -> A -> D
        setAuthentication(userA);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdD);
        setAuthentication(userB);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        setAuthentication(userC);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        setAuthentication(userD);
        postService.pressZzang(postIdB);
        setAuthentication(userE);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdA);

        RankingRequest rankingRequest = new RankingRequest(TOTAL.getCriteriaName());
        PostSearchRequest postSearchRequest = new PostSearchRequest(
            null, null);
        Page<PostWithCommentsCountResponse> posts
            = postService.searchRanking(rankingRequest, Pageable.unpaged(), postSearchRequest);

        assertThat(posts.getContent()).hasSize(4);
        assertThat(posts.getContent().get(0).getId()).isEqualTo(postIdB);
        assertThat(posts.getContent().get(1).getId()).isEqualTo(postIdC);
        assertThat(posts.getContent().get(2).getId()).isEqualTo(postIdA);
        assertThat(posts.getContent().get(3).getId()).isEqualTo(postIdD);
    }

    @DisplayName("모든 부문, 모든 지역 랭킹 조회 - 어제")
    @Test
    @Transactional
    void searchRanking_AllSectorAllAreaYesterday_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorOtherId);
        Long postIdD = postService.createPost(postCreateRequest).getId();

        //PostA 짱이야2 - 어제 1 오늘 1
        //PostB 짱이야4 - 어제 2 오늘 2
        //PostC 짱이야3 - 어제 3 오늘 0
        //PostD 짱이야1 - 어제 0 오늘 1
        //랭킹 순서: C -> B -> A -> D
        setAuthentication(userA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdD);
        setAuthentication(userB);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdA);
        setAuthentication(userC);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        setAuthentication(userD);
        postService.pressZzang(postIdB);
        setAuthentication(userE);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdA);

        Post postA = postRepository.findById(postIdA)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));
        Post postB = postRepository.findById(postIdB)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));
        Post postC = postRepository.findById(postIdC)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));

        postA.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusDays(1));
        postB.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusDays(1));
        postB.getZzangs().get(1).setCreatedAt(LocalDateTime.now().minusDays(1));
        postC.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusDays(1));
        postC.getZzangs().get(1).setCreatedAt(LocalDateTime.now().minusDays(1));
        postC.getZzangs().get(2).setCreatedAt(LocalDateTime.now().minusDays(1));

        RankingRequest rankingRequest = new RankingRequest(YESTERDAY.getCriteriaName());
        PostSearchRequest postSearchRequest = new PostSearchRequest(
            null, null);
        Page<PostWithCommentsCountResponse> posts
            = postService.searchRanking(rankingRequest, Pageable.unpaged(), postSearchRequest);

        assertThat(posts.getContent()).hasSize(4);
        assertThat(posts.getContent().get(0).getId()).isEqualTo(postIdC);
        assertThat(posts.getContent().get(1).getId()).isEqualTo(postIdB);
        assertThat(posts.getContent().get(2).getId()).isEqualTo(postIdA);
        assertThat(posts.getContent().get(3).getId()).isEqualTo(postIdD);
    }

    @DisplayName("모든 부문, 모든 지역 랭킹 조회 - 지난 주")
    @Test
    @Transactional
    void searchRanking_AllSectorAllAreaLastWeek_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorOtherId);
        Long postIdD = postService.createPost(postCreateRequest).getId();

        //PostA 짱이야2 - 지난 주 2 오늘 0
        //PostB 짱이야4 - 지난 주 0 오늘 4
        //PostC 짱이야3 - 지난 주  2 오늘 1
        //PostD 짱이야1 - 지난 주 0 오늘 1
        //랭킹 순서: C -> A -> D -> B (동률일 경우 최신 글이 상위로)
        setAuthentication(userA);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdD);
        setAuthentication(userB);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        setAuthentication(userC);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        setAuthentication(userD);
        postService.pressZzang(postIdB);
        setAuthentication(userE);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdA);

        Post postA = postRepository.findById(postIdA)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));
        Post postC = postRepository.findById(postIdC)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));

        postA.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusWeeks(1));
        postA.getZzangs().get(1).setCreatedAt(LocalDateTime.now().minusWeeks(1));
        postC.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusWeeks(1));
        postC.getZzangs().get(1).setCreatedAt(LocalDateTime.now().minusWeeks(1));

        RankingRequest rankingRequest = new RankingRequest(LAST_WEEK.getCriteriaName());
        PostSearchRequest postSearchRequest = new PostSearchRequest(
            null, null);
        Page<PostWithCommentsCountResponse> posts
            = postService.searchRanking(rankingRequest, Pageable.unpaged(), postSearchRequest);

        assertThat(posts.getContent()).hasSize(4);
        assertThat(posts.getContent().get(0).getId()).isEqualTo(postIdC);
        assertThat(posts.getContent().get(1).getId()).isEqualTo(postIdA);
        assertThat(posts.getContent().get(2).getId()).isEqualTo(postIdD);
        assertThat(posts.getContent().get(3).getId()).isEqualTo(postIdB);
    }

    @DisplayName("모든 부문, 모든 지역 랭킹 조회 - 지난 달")
    @Test
    @Transactional
    void searchRanking_AllSectorAllAreaLastMonth_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorOtherId);
        Long postIdD = postService.createPost(postCreateRequest).getId();

        //PostA 짱이야2 - 지난달 1 오늘 1
        //PostB 짱이야4 - 지난달 0 오늘 4
        //PostC 짱이야3 - 지난달 0 오늘 3
        //PostD 짱이야1 - 지난달 1 오늘 0
        //랭킹 순서: D -> A -> C -> B (동률일 경우 최신 글이 상위로)
        setAuthentication(userA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdD);
        setAuthentication(userB);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdA);
        setAuthentication(userC);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        setAuthentication(userD);
        postService.pressZzang(postIdB);
        setAuthentication(userE);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdA);

        Post postA = postRepository.findById(postIdA)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));
        Post postD = postRepository.findById(postIdD)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));

        postA.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusMonths(1));
        postD.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusMonths(1));

        RankingRequest rankingRequest = new RankingRequest(LAST_MONTH.getCriteriaName());
        PostSearchRequest postSearchRequest = new PostSearchRequest(
            null, null);
        Page<PostWithCommentsCountResponse> posts
            = postService.searchRanking(rankingRequest, Pageable.unpaged(), postSearchRequest);

        assertThat(posts.getContent()).hasSize(4);
        assertThat(posts.getContent().get(0).getId()).isEqualTo(postIdD);
        assertThat(posts.getContent().get(1).getId()).isEqualTo(postIdA);
        assertThat(posts.getContent().get(2).getId()).isEqualTo(postIdC);
        assertThat(posts.getContent().get(3).getId()).isEqualTo(postIdB);
    }

    @DisplayName("특정 부문, 모든 지역 랭킹 조회 - 전체 기간")
    @Test
    void searchRanking_SpecificSectorAllAreaTotalCriteria_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorOtherId);
        Long postIdD = postService.createPost(postCreateRequest).getId();

        //PostA 짱이야2 -> 선택 부문
        //PostB 짱이야4
        //PostC 짱이야3 -> 선택 부문
        //PostD 짱이야1
        //랭킹 순서: C -> A
        setAuthentication(userA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdD);
        setAuthentication(userB);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdA);
        setAuthentication(userC);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        setAuthentication(userD);
        postService.pressZzang(postIdB);
        setAuthentication(userE);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdA);

        RankingRequest rankingRequest = new RankingRequest(TOTAL.getCriteriaName());
        PostSearchRequest postSearchRequest = new PostSearchRequest(
            null, String.valueOf(sectorId));
        Page<PostWithCommentsCountResponse> posts
            = postService.searchRanking(rankingRequest, Pageable.unpaged(), postSearchRequest);

        assertThat(posts.getContent()).hasSize(2);
        assertThat(posts.getContent().get(0).getId()).isEqualTo(postIdC);
        assertThat(posts.getContent().get(1).getId()).isEqualTo(postIdA);
    }

    @DisplayName("특정 부문, 모든 지역 랭킹 조회 - 어제")
    @Test
    @Transactional
    void searchRanking_SpecificSectorAllAreaYesterday_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorOtherId);
        Long postIdD = postService.createPost(postCreateRequest).getId();

        //PostA 짱이야2 - 어제 4 오늘 0 -> 선택 부문
        //PostB 짱이야4 - 어제 2 오늘 2
        //PostC 짱이야3 - 어제 3 오늘 0 -> 선택 부문
        //PostD 짱이야1 - 어제 0 오늘 1
        //랭킹 순서: A -> C
        setAuthentication(userA);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdD);
        setAuthentication(userB);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        setAuthentication(userC);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        setAuthentication(userD);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdB);
        setAuthentication(userE);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdA);

        Post postA = postRepository.findById(postIdA)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));
        Post postB = postRepository.findById(postIdB)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));
        Post postC = postRepository.findById(postIdC)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));

        postA.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusDays(1));
        postA.getZzangs().get(1).setCreatedAt(LocalDateTime.now().minusDays(1));
        postA.getZzangs().get(2).setCreatedAt(LocalDateTime.now().minusDays(1));
        postA.getZzangs().get(3).setCreatedAt(LocalDateTime.now().minusDays(1));
        postB.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusDays(1));
        postB.getZzangs().get(1).setCreatedAt(LocalDateTime.now().minusDays(1));
        postC.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusDays(1));
        postC.getZzangs().get(1).setCreatedAt(LocalDateTime.now().minusDays(1));
        postC.getZzangs().get(2).setCreatedAt(LocalDateTime.now().minusDays(1));

        RankingRequest rankingRequest = new RankingRequest(YESTERDAY.getCriteriaName());
        PostSearchRequest postSearchRequest = new PostSearchRequest(
            null, String.valueOf(sectorId));
        Page<PostWithCommentsCountResponse> posts
            = postService.searchRanking(rankingRequest, Pageable.unpaged(), postSearchRequest);

        assertThat(posts.getContent()).hasSize(2);
        assertThat(posts.getContent().get(0).getId()).isEqualTo(postIdA);
        assertThat(posts.getContent().get(1).getId()).isEqualTo(postIdC);
    }

    @DisplayName("특정 부문, 모든 지역 랭킹 조회 - 지난 주")
    @Test
    @Transactional
    void searchRanking_SpecificSectorAllAreaLastWeek_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorOtherId);
        Long postIdD = postService.createPost(postCreateRequest).getId();

        //PostA 짱이야2 - 지난 주 2 오늘 0 -> 선택 부문
        //PostB 짱이야4 - 지난 주 0 오늘 4
        //PostC 짱이야3 - 지난 주  2 오늘 1 -> 선택 부문
        //PostD 짱이야1 - 지난 주 0 오늘 1
        //랭킹 순서: C -> A (동률일 경우 최신 글이 상위로)
        setAuthentication(userA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdD);
        setAuthentication(userB);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdA);
        setAuthentication(userC);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        setAuthentication(userD);
        postService.pressZzang(postIdB);
        setAuthentication(userE);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdA);

        Post postA = postRepository.findById(postIdA)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));
        Post postC = postRepository.findById(postIdC)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));

        postC.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusWeeks(1));
        postC.getZzangs().get(1).setCreatedAt(LocalDateTime.now().minusWeeks(1));
        postA.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusWeeks(1));
        postA.getZzangs().get(1).setCreatedAt(LocalDateTime.now().minusWeeks(1));

        RankingRequest rankingRequest = new RankingRequest(LAST_WEEK.getCriteriaName());
        PostSearchRequest postSearchRequest = new PostSearchRequest(
            null, String.valueOf(sectorId));
        Page<PostWithCommentsCountResponse> posts
            = postService.searchRanking(rankingRequest, Pageable.unpaged(), postSearchRequest);

        assertThat(posts.getContent()).hasSize(2);
        assertThat(posts.getContent().get(0).getId()).isEqualTo(postIdC);
        assertThat(posts.getContent().get(1).getId()).isEqualTo(postIdA);
    }

    @DisplayName("특정 부문, 모든 지역 랭킹 조회 - 지난 달")
    @Test
    @Transactional
    void searchRanking_SpecificSectorAllAreaLastMonth_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorOtherId);
        Long postIdD = postService.createPost(postCreateRequest).getId();

        //PostA 짱이야2 - 지난달 1 오늘 1 -> 선택 부문
        //PostB 짱이야4 - 지난달 0 오늘 4
        //PostC 짱이야3 - 지난달 0 오늘 3 -> 선택 부문
        //PostD 짱이야1 - 지난달 1 오늘 0
        //랭킹 순서: A -> C
        setAuthentication(userA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdD);
        setAuthentication(userB);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdA);
        setAuthentication(userC);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        setAuthentication(userD);
        postService.pressZzang(postIdB);
        setAuthentication(userE);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdA);

        Post postA = postRepository.findById(postIdA)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));
        Post postD = postRepository.findById(postIdD)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));

        postA.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusMonths(1));
        postD.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusMonths(1));

        RankingRequest rankingRequest = new RankingRequest(LAST_MONTH.getCriteriaName());
        PostSearchRequest postSearchRequest = new PostSearchRequest(
            null, String.valueOf(sectorId));
        Page<PostWithCommentsCountResponse> posts
            = postService.searchRanking(rankingRequest, Pageable.unpaged(), postSearchRequest);

        assertThat(posts.getContent()).hasSize(2);
        assertThat(posts.getContent().get(0).getId()).isEqualTo(postIdA);
        assertThat(posts.getContent().get(1).getId()).isEqualTo(postIdC);
    }

    @DisplayName("모든 부문, 특정 지역 랭킹 조회 - 전체 기간")
    @Test
    void searchRanking_ALLSectorSpecificAreaTotalCriteria_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorOtherId);
        Long postIdD = postService.createPost(postCreateRequest).getId();

        //PostA 짱이야2 -> 선택 지역
        //PostB 짱이야4 -> 선택 지역
        //PostC 짱이야3
        //PostD 짱이야1
        //랭킹 순서: B -> A
        setAuthentication(userA);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdD);
        setAuthentication(userB);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        setAuthentication(userC);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        setAuthentication(userD);
        postService.pressZzang(postIdB);
        setAuthentication(userE);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdA);

        RankingRequest rankingRequest = new RankingRequest(TOTAL.getCriteriaName());
        PostSearchRequest postSearchRequest = new PostSearchRequest(
            String.valueOf(TEST_AREA_ID), null);
        Page<PostWithCommentsCountResponse> posts
            = postService.searchRanking(rankingRequest, Pageable.unpaged(), postSearchRequest);

        assertThat(posts.getContent()).hasSize(2);
        assertThat(posts.getContent().get(0).getId()).isEqualTo(postIdB);
        assertThat(posts.getContent().get(1).getId()).isEqualTo(postIdA);
    }

    @DisplayName("모든 부문, 특정 지역 랭킹 조회 - 어제")
    @Test
    @Transactional
    void searchRanking_AllSectorSpecificAreaYesterday_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorOtherId);
        Long postIdD = postService.createPost(postCreateRequest).getId();

        //PostA 짱이야2 - 어제 1 오늘 1 -> 선택 지역
        //PostB 짱이야4 - 어제 2 오늘 2 -> 선택 지역
        //PostC 짱이야3 - 어제 3 오늘 0
        //PostD 짱이야1 - 어제 0 오늘 1
        //랭킹 순서: B -> A
        setAuthentication(userA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdD);
        setAuthentication(userB);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdA);
        setAuthentication(userC);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        setAuthentication(userD);
        postService.pressZzang(postIdB);
        setAuthentication(userE);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdA);

        Post postA = postRepository.findById(postIdA)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));
        Post postB = postRepository.findById(postIdB)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));
        Post postC = postRepository.findById(postIdC)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));

        postA.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusDays(1));
        postB.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusDays(1));
        postB.getZzangs().get(1).setCreatedAt(LocalDateTime.now().minusDays(1));
        postC.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusDays(1));
        postC.getZzangs().get(1).setCreatedAt(LocalDateTime.now().minusDays(1));
        postC.getZzangs().get(2).setCreatedAt(LocalDateTime.now().minusDays(1));

        RankingRequest rankingRequest = new RankingRequest(YESTERDAY.getCriteriaName());
        PostSearchRequest postSearchRequest = new PostSearchRequest(
            String.valueOf(TEST_AREA_ID), null);
        Page<PostWithCommentsCountResponse> posts
            = postService.searchRanking(rankingRequest, Pageable.unpaged(), postSearchRequest);

        assertThat(posts.getContent()).hasSize(2);
        assertThat(posts.getContent().get(0).getId()).isEqualTo(postIdB);
        assertThat(posts.getContent().get(1).getId()).isEqualTo(postIdA);
    }

    @DisplayName("모든 부문, 특정 지역 랭킹 조회 - 지난 주")
    @Test
    @Transactional
    void searchRanking_AllSectorSpecificAreaLastWeek_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorOtherId);
        Long postIdD = postService.createPost(postCreateRequest).getId();

        //PostA 짱이야2 - 지난 주 2 오늘 0 -> 선택 지역
        //PostB 짱이야4 - 지난 주 0 오늘 4 -> 선택 지역
        //PostC 짱이야3 - 지난 주  2 오늘 1
        //PostD 짱이야1 - 지난 주 0 오늘 1
        //랭킹 순서: A -> B
        setAuthentication(userA);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdD);
        setAuthentication(userB);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        setAuthentication(userC);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        setAuthentication(userD);
        postService.pressZzang(postIdB);
        setAuthentication(userE);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdA);

        Post postA = postRepository.findById(postIdA)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));
        Post postC = postRepository.findById(postIdC)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));

        postA.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusWeeks(1));
        postA.getZzangs().get(1).setCreatedAt(LocalDateTime.now().minusWeeks(1));
        postC.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusWeeks(1));
        postC.getZzangs().get(1).setCreatedAt(LocalDateTime.now().minusWeeks(1));

        RankingRequest rankingRequest = new RankingRequest(LAST_WEEK.getCriteriaName());
        PostSearchRequest postSearchRequest = new PostSearchRequest(
            String.valueOf(TEST_AREA_ID), null);
        Page<PostWithCommentsCountResponse> posts
            = postService.searchRanking(rankingRequest, Pageable.unpaged(), postSearchRequest);

        assertThat(posts.getContent()).hasSize(2);
        assertThat(posts.getContent().get(0).getId()).isEqualTo(postIdA);
        assertThat(posts.getContent().get(1).getId()).isEqualTo(postIdB);
    }

    @DisplayName("모든 부문, 특정 지역 랭킹 조회 - 지난 달")
    @Test
    @Transactional
    void searchRanking_AllSectorSpecificAreaLastMonth_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorOtherId);
        Long postIdD = postService.createPost(postCreateRequest).getId();

        //PostA 짱이야2 - 지난달 1 오늘 1 -> 선택 지역
        //PostB 짱이야4 - 지난달 0 오늘 4 -> 선택 지역
        //PostC 짱이야3 - 지난달 0 오늘 3
        //PostD 짱이야1 - 지난달 1 오늘 0
        //랭킹 순서: A -> B
        setAuthentication(userA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdD);
        setAuthentication(userB);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdA);
        setAuthentication(userC);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        setAuthentication(userD);
        postService.pressZzang(postIdB);
        setAuthentication(userE);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdA);

        Post postA = postRepository.findById(postIdA)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));
        Post postD = postRepository.findById(postIdD)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));

        postA.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusMonths(1));
        postD.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusMonths(1));

        RankingRequest rankingRequest = new RankingRequest(LAST_MONTH.getCriteriaName());
        PostSearchRequest postSearchRequest = new PostSearchRequest(
            String.valueOf(TEST_AREA_ID), null);
        Page<PostWithCommentsCountResponse> posts
            = postService.searchRanking(rankingRequest, Pageable.unpaged(), postSearchRequest);

        assertThat(posts.getContent()).hasSize(2);
        assertThat(posts.getContent().get(0).getId()).isEqualTo(postIdA);
        assertThat(posts.getContent().get(1).getId()).isEqualTo(postIdB);
    }

    @DisplayName("특정 부문, 특정 지역 랭킹 조회 - 전체 기간")
    @Test
    void searchRanking_SpecificSectorSpecificAreaTotalCriteria_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        Long postIdD = postService.createPost(postCreateRequest).getId();

        //PostA 짱이야2 -> 선택 부문, 선택 지역
        //PostB 짱이야4
        //PostC 짱이야3
        //PostD 짱이야1 -> 선택 부문, 선택 지역
        //랭킹 순서: A -> D
        setAuthentication(userA);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdD);
        setAuthentication(userB);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        setAuthentication(userC);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        setAuthentication(userD);
        postService.pressZzang(postIdB);
        setAuthentication(userE);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdA);

        RankingRequest rankingRequest = new RankingRequest(TOTAL.getCriteriaName());
        PostSearchRequest postSearchRequest = new PostSearchRequest(
            String.valueOf(TEST_AREA_ID), String.valueOf(sectorId));
        Page<PostWithCommentsCountResponse> posts
            = postService.searchRanking(rankingRequest, Pageable.unpaged(), postSearchRequest);

        assertThat(posts.getContent()).hasSize(2);
        assertThat(posts.getContent().get(0).getId()).isEqualTo(postIdA);
        assertThat(posts.getContent().get(1).getId()).isEqualTo(postIdD);
    }

    @DisplayName("특정 부문, 특정 지역 랭킹 조회 - 어제")
    @Test
    @Transactional
    void searchRanking_SpecificSectorSpecificAreaYesterday_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        Long postIdD = postService.createPost(postCreateRequest).getId();

        //PostA 짱이야2 - 어제 1 오늘 1 -> 선택 부문, 선택 지역
        //PostB 짱이야4 - 어제 2 오늘 2
        //PostC 짱이야3 - 어제 3 오늘 0
        //PostD 짱이야1 - 어제 0 오늘 1 -> 선택 부문, 선택 지역
        //랭킹 순서: A -> D
        setAuthentication(userA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdD);
        setAuthentication(userB);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdA);
        setAuthentication(userC);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        setAuthentication(userD);
        postService.pressZzang(postIdB);
        setAuthentication(userE);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdA);

        Post postA = postRepository.findById(postIdA)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));
        Post postB = postRepository.findById(postIdB)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));
        Post postC = postRepository.findById(postIdC)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));

        postA.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusDays(1));
        postB.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusDays(1));
        postB.getZzangs().get(1).setCreatedAt(LocalDateTime.now().minusDays(1));
        postC.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusDays(1));
        postC.getZzangs().get(1).setCreatedAt(LocalDateTime.now().minusDays(1));
        postC.getZzangs().get(2).setCreatedAt(LocalDateTime.now().minusDays(1));

        RankingRequest rankingRequest = new RankingRequest(YESTERDAY.getCriteriaName());
        PostSearchRequest postSearchRequest = new PostSearchRequest(
            String.valueOf(TEST_AREA_ID), String.valueOf(sectorId));
        Page<PostWithCommentsCountResponse> posts
            = postService.searchRanking(rankingRequest, Pageable.unpaged(), postSearchRequest);

        assertThat(posts.getContent()).hasSize(2);
        assertThat(posts.getContent().get(0).getId()).isEqualTo(postIdA);
        assertThat(posts.getContent().get(1).getId()).isEqualTo(postIdD);
    }

    @DisplayName("특정 부문, 특정 지역 랭킹 조회 - 지난 주")
    @Test
    @Transactional
    void searchRanking_SpecificSectorSpecificAreaLastWeek_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        Long postIdD = postService.createPost(postCreateRequest).getId();

        //PostA 짱이야2 - 지난 주 2 오늘 0 -> 선택 부문, 선택 지역
        //PostB 짱이야4 - 지난 주 0 오늘 4
        //PostC 짱이야3 - 지난 주  2 오늘 1
        //PostD 짱이야1 - 지난 주 0 오늘 1 -> 선택 부문, 선택 지역
        //랭킹 순서: A -> D
        setAuthentication(userA);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdD);
        setAuthentication(userB);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        setAuthentication(userC);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        setAuthentication(userD);
        postService.pressZzang(postIdB);
        setAuthentication(userE);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdA);

        Post postA = postRepository.findById(postIdA)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));
        Post postC = postRepository.findById(postIdC)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));

        postA.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusWeeks(1));
        postA.getZzangs().get(1).setCreatedAt(LocalDateTime.now().minusWeeks(1));
        postC.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusWeeks(1));
        postC.getZzangs().get(1).setCreatedAt(LocalDateTime.now().minusWeeks(1));

        RankingRequest rankingRequest = new RankingRequest(LAST_WEEK.getCriteriaName());
        PostSearchRequest postSearchRequest = new PostSearchRequest(
            String.valueOf(TEST_AREA_ID), String.valueOf(sectorId));
        Page<PostWithCommentsCountResponse> posts
            = postService.searchRanking(rankingRequest, Pageable.unpaged(), postSearchRequest);

        assertThat(posts.getContent()).hasSize(2);
        assertThat(posts.getContent().get(0).getId()).isEqualTo(postIdA);
        assertThat(posts.getContent().get(1).getId()).isEqualTo(postIdD);
    }

    @DisplayName("특정 부문, 특정 지역 랭킹 조회 - 지난 달")
    @Test
    @Transactional
    void searchRanking_SpecificSectorSpecificAreaLastMonth_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        Long postIdD = postService.createPost(postCreateRequest).getId();

        //PostA 짱이야2 - 지난달 1 오늘 1 -> 선택 부문, 선택 지역
        //PostB 짱이야4 - 지난달 0 오늘 4
        //PostC 짱이야3 - 지난달 0 오늘 3
        //PostD 짱이야1 - 지난달 1 오늘 0 -> 선택 부문, 선택 지역
        //랭킹 순서: D -> A (동률일 경우 최신 글이 상위로)
        setAuthentication(userA);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdD);
        setAuthentication(userB);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        postService.pressZzang(postIdA);
        setAuthentication(userC);
        postService.pressZzang(postIdB);
        postService.pressZzang(postIdC);
        setAuthentication(userD);
        postService.pressZzang(postIdB);
        setAuthentication(userE);
        postService.pressZzang(postIdA);
        postService.pressZzang(postIdA);

        Post postA = postRepository.findById(postIdA)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));
        Post postD = postRepository.findById(postIdD)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));

        postA.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusMonths(1));
        postD.getZzangs().get(0).setCreatedAt(LocalDateTime.now().minusMonths(1));

        RankingRequest rankingRequest = new RankingRequest(LAST_MONTH.getCriteriaName());
        PostSearchRequest postSearchRequest = new PostSearchRequest(
            String.valueOf(TEST_AREA_ID), String.valueOf(sectorId));
        Page<PostWithCommentsCountResponse> posts
            = postService.searchRanking(rankingRequest, Pageable.unpaged(), postSearchRequest);

        assertThat(posts.getContent()).hasSize(2);
        assertThat(posts.getContent().get(0).getId()).isEqualTo(postIdD);
        assertThat(posts.getContent().get(1).getId()).isEqualTo(postIdA);
    }
}
