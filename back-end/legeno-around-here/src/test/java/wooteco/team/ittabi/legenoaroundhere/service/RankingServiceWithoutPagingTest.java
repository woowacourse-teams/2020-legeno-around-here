package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static wooteco.team.ittabi.legenoaroundhere.domain.ranking.RankingCriteria.LAST_MONTH;
import static wooteco.team.ittabi.legenoaroundhere.domain.ranking.RankingCriteria.LAST_WEEK;
import static wooteco.team.ittabi.legenoaroundhere.domain.ranking.RankingCriteria.TOTAL;
import static wooteco.team.ittabi.legenoaroundhere.domain.ranking.RankingCriteria.YESTERDAY;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_OTHER_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AUTH_NUMBER;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_EMPTY_IMAGES;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_POST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_ANOTHER_REQUEST;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_REQUEST;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.domain.user.mailauth.MailAuth;
import wooteco.team.ittabi.legenoaroundhere.dto.PostCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostSearchRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostWithCommentsCountResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.RankingRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.repository.MailAuthRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.PostRepository;

public class RankingServiceWithoutPagingTest extends ServiceTest {

    private static final String TEST_PREFIX = "ranking_";

    @MockBean
    private MailAuthRepository mailAuthRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RankingService rankingService;

    @Autowired
    private SectorService sectorService;

    private User userA;
    private User userB;
    private User userC;
    private User userD;
    private User userE;
    private Long sectorId;
    private Long sectorOtherId;

    @BeforeEach
    void setUp() {
        MailAuth mailAuth = new MailAuth(TEST_USER_EMAIL, TEST_AUTH_NUMBER);
        when(mailAuthRepository.findByEmail(any())).thenReturn(java.util.Optional.of(mailAuth));

        User user = createUser(TEST_PREFIX + TEST_USER_EMAIL,
            TEST_USER_NICKNAME,
            TEST_USER_PASSWORD);
        userA = createUser(TEST_USER_EMAIL + "A", TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        userB = createUser(TEST_USER_EMAIL + "B", TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        userC = createUser(TEST_USER_EMAIL + "C", TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        userD = createUser(TEST_USER_EMAIL + "D", TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        userE = createUser(TEST_USER_EMAIL + "E", TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        setAuthentication(user);

        SectorResponse sector = sectorService.createSector(TEST_SECTOR_REQUEST);
        sectorId = sector.getId();
        sectorOtherId = sectorService.createSector(TEST_SECTOR_ANOTHER_REQUEST).getId();
    }

    @DisplayName("모든 부문, 모든 지역 랭킹 조회 - 전체 기간")
    @Test
    void searchRanking_AllSectorAllAreaTotalCriteria_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorOtherId);
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
        List<Post> posts = rankingService.searchRanking(postSearchRequest, rankingRequest, 10);

        assertThat(posts).hasSize(4);
        assertThat(posts.get(0).getId()).isEqualTo(postIdB);
        assertThat(posts.get(1).getId()).isEqualTo(postIdC);
        assertThat(posts.get(2).getId()).isEqualTo(postIdA);
        assertThat(posts.get(3).getId()).isEqualTo(postIdD);
    }

    @DisplayName("모든 부문, 모든 지역 랭킹 조회 - 어제")
    @Test
    @Transactional
    void searchRanking_AllSectorAllAreaYesterday_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorOtherId);
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
        List<Post> posts = rankingService.searchRanking(postSearchRequest, rankingRequest, 10);

        assertThat(posts).hasSize(4);
        assertThat(posts.get(0).getId()).isEqualTo(postIdC);
        assertThat(posts.get(1).getId()).isEqualTo(postIdB);
        assertThat(posts.get(2).getId()).isEqualTo(postIdA);
        assertThat(posts.get(3).getId()).isEqualTo(postIdD);
    }

    @DisplayName("모든 부문, 모든 지역 랭킹 조회 - 지난주")
    @Test
    @Transactional
    void searchRanking_AllSectorAllAreaLastWeek_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorOtherId);
        Long postIdD = postService.createPost(postCreateRequest).getId();

        //PostA 짱이야2 - 지난주 2 오늘 0
        //PostB 짱이야4 - 지난주 0 오늘 4
        //PostC 짱이야3 - 지난주  2 오늘 1
        //PostD 짱이야1 - 지난주 0 오늘 1
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
        List<Post> posts = rankingService.searchRanking(postSearchRequest, rankingRequest, 10);

        assertThat(posts).hasSize(4);
        assertThat(posts.get(0).getId()).isEqualTo(postIdC);
        assertThat(posts.get(1).getId()).isEqualTo(postIdA);
        assertThat(posts.get(2).getId()).isEqualTo(postIdD);
        assertThat(posts.get(3).getId()).isEqualTo(postIdB);
    }

    @DisplayName("모든 부문, 모든 지역 랭킹 조회 - 지난달")
    @Test
    @Transactional
    void searchRanking_AllSectorAllAreaLastMonth_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorOtherId);
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
        List<Post> posts = rankingService.searchRanking(postSearchRequest, rankingRequest, 10);

        assertThat(posts).hasSize(4);
        assertThat(posts.get(0).getId()).isEqualTo(postIdD);
        assertThat(posts.get(1).getId()).isEqualTo(postIdA);
        assertThat(posts.get(2).getId()).isEqualTo(postIdC);
        assertThat(posts.get(3).getId()).isEqualTo(postIdB);
    }

    @DisplayName("특정 부문, 모든 지역 랭킹 조회 - 전체 기간")
    @Test
    void searchRanking_SpecificSectorAllAreaTotalCriteria_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorOtherId);
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
        List<Post> posts = rankingService.searchRanking(postSearchRequest, rankingRequest, 10);

        assertThat(posts).hasSize(2);
        assertThat(posts.get(0).getId()).isEqualTo(postIdC);
        assertThat(posts.get(1).getId()).isEqualTo(postIdA);
    }

    @DisplayName("특정 부문, 모든 지역 랭킹 조회 - 어제")
    @Test
    @Transactional
    void searchRanking_SpecificSectorAllAreaYesterday_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorOtherId);
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
        List<Post> posts = rankingService.searchRanking(postSearchRequest, rankingRequest, 10);

        assertThat(posts).hasSize(2);
        assertThat(posts.get(0).getId()).isEqualTo(postIdA);
        assertThat(posts.get(1).getId()).isEqualTo(postIdC);
    }

    @DisplayName("특정 부문, 모든 지역 랭킹 조회 - 지난주")
    @Test
    @Transactional
    void searchRanking_SpecificSectorAllAreaLastWeek_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorOtherId);
        Long postIdD = postService.createPost(postCreateRequest).getId();

        //PostA 짱이야2 - 지난주 2 오늘 0 -> 선택 부문
        //PostB 짱이야4 - 지난주 0 오늘 4
        //PostC 짱이야3 - 지난주  2 오늘 1 -> 선택 부문
        //PostD 짱이야1 - 지난주 0 오늘 1
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
        List<Post> posts = rankingService.searchRanking(postSearchRequest, rankingRequest, 10);

        assertThat(posts).hasSize(2);
        assertThat(posts.get(0).getId()).isEqualTo(postIdC);
        assertThat(posts.get(1).getId()).isEqualTo(postIdA);
    }

    @DisplayName("특정 부문, 모든 지역 랭킹 조회 - 지난달")
    @Test
    @Transactional
    void searchRanking_SpecificSectorAllAreaLastMonth_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorOtherId);
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
        List<Post> posts = rankingService.searchRanking(postSearchRequest, rankingRequest, 10);

        assertThat(posts).hasSize(2);
        assertThat(posts.get(0).getId()).isEqualTo(postIdA);
        assertThat(posts.get(1).getId()).isEqualTo(postIdC);
    }

    @DisplayName("모든 부문, 특정 지역 랭킹 조회 - 전체 기간")
    @Test
    void searchRanking_ALLSectorSpecificAreaTotalCriteria_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorOtherId);
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
            TEST_AREA_ID, null);
        List<Post> posts = rankingService.searchRanking(postSearchRequest, rankingRequest, 10);

        assertThat(posts).hasSize(2);
        assertThat(posts.get(0).getId()).isEqualTo(postIdB);
        assertThat(posts.get(1).getId()).isEqualTo(postIdA);
    }

    @DisplayName("모든 부문, 특정 지역 랭킹 조회 - 어제")
    @Test
    @Transactional
    void searchRanking_AllSectorSpecificAreaYesterday_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorOtherId);
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
            TEST_AREA_ID, null);
        List<Post> posts = rankingService.searchRanking(postSearchRequest, rankingRequest, 10);

        assertThat(posts).hasSize(2);
        assertThat(posts.get(0).getId()).isEqualTo(postIdB);
        assertThat(posts.get(1).getId()).isEqualTo(postIdA);
    }

    @DisplayName("모든 부문, 특정 지역 랭킹 조회 - 지난주")
    @Test
    @Transactional
    void searchRanking_AllSectorSpecificAreaLastWeek_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorOtherId);
        Long postIdD = postService.createPost(postCreateRequest).getId();

        //PostA 짱이야2 - 지난주 2 오늘 0 -> 선택 지역
        //PostB 짱이야4 - 지난주 0 오늘 4 -> 선택 지역
        //PostC 짱이야3 - 지난주  2 오늘 1
        //PostD 짱이야1 - 지난주 0 오늘 1
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
            TEST_AREA_ID, null);
        Page<PostWithCommentsCountResponse> posts
            = rankingService.searchRanking(Pageable.unpaged(), postSearchRequest, rankingRequest);

        assertThat(posts.getContent()).hasSize(2);
        assertThat(posts.getContent().get(0).getId()).isEqualTo(postIdA);
        assertThat(posts.getContent().get(1).getId()).isEqualTo(postIdB);
    }

    @DisplayName("모든 부문, 특정 지역 랭킹 조회 - 지난달")
    @Test
    @Transactional
    void searchRanking_AllSectorSpecificAreaLastMonth_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorOtherId);
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
            TEST_AREA_ID, null);
        List<Post> posts = rankingService.searchRanking(postSearchRequest, rankingRequest, 10);

        assertThat(posts).hasSize(2);
        assertThat(posts.get(0).getId()).isEqualTo(postIdA);
        assertThat(posts.get(1).getId()).isEqualTo(postIdB);
    }

    @DisplayName("특정 부문, 특정 지역 랭킹 조회 - 전체 기간")
    @Test
    void searchRanking_SpecificSectorSpecificAreaTotalCriteria_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);
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
            TEST_AREA_ID, String.valueOf(sectorId));
        List<Post> posts = rankingService.searchRanking(postSearchRequest, rankingRequest, 10);

        assertThat(posts).hasSize(2);
        assertThat(posts.get(0).getId()).isEqualTo(postIdA);
        assertThat(posts.get(1).getId()).isEqualTo(postIdD);
    }

    @DisplayName("특정 부문, 특정 지역 랭킹 조회 - 어제")
    @Test
    @Transactional
    void searchRanking_SpecificSectorSpecificAreaYesterday_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);
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
            TEST_AREA_ID, String.valueOf(sectorId));
        List<Post> posts = rankingService.searchRanking(postSearchRequest, rankingRequest, 10);

        assertThat(posts).hasSize(2);
        assertThat(posts.get(0).getId()).isEqualTo(postIdA);
        assertThat(posts.get(1).getId()).isEqualTo(postIdD);
    }

    @DisplayName("특정 부문, 특정 지역 랭킹 조회 - 지난주")
    @Test
    @Transactional
    void searchRanking_SpecificSectorSpecificAreaLastWeek_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);
        Long postIdD = postService.createPost(postCreateRequest).getId();

        //PostA 짱이야2 - 지난주 2 오늘 0 -> 선택 부문, 선택 지역
        //PostB 짱이야4 - 지난주 0 오늘 4
        //PostC 짱이야3 - 지난주  2 오늘 1
        //PostD 짱이야1 - 지난주 0 오늘 1 -> 선택 부문, 선택 지역
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
            TEST_AREA_ID, String.valueOf(sectorId));
        List<Post> posts = rankingService.searchRanking(postSearchRequest, rankingRequest, 10);

        assertThat(posts).hasSize(2);
        assertThat(posts.get(0).getId()).isEqualTo(postIdA);
        assertThat(posts.get(1).getId()).isEqualTo(postIdD);
    }

    @DisplayName("특정 부문, 특정 지역 랭킹 조회 - 지난달")
    @Test
    @Transactional
    void searchRanking_SpecificSectorSpecificAreaLastMonth_SuccessToFind() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorOtherId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);
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
            TEST_AREA_ID, String.valueOf(sectorId));
        Page<PostWithCommentsCountResponse> posts
            = rankingService.searchRanking(Pageable.unpaged(), postSearchRequest, rankingRequest);

        assertThat(posts.getContent()).hasSize(2);
        assertThat(posts.getContent().get(0).getId()).isEqualTo(postIdD);
        assertThat(posts.getContent().get(1).getId()).isEqualTo(postIdA);
    }
}
