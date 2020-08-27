package wooteco.team.ittabi.legenoaroundhere.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_POST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_ID;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.ranking.RankingCriteria;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.SectorState;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.PageRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PageableAssembler;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/init-table.sql")
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SectorRepository sectorRepository;

    private Long sectorId;
    private Long postId;

    @BeforeEach
    void setUp() {
        Area area = areaRepository.findById(TEST_AREA_ID)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 Area가 없습니다."));
        User user = userRepository.findById(TEST_USER_ID)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 User가 없습니다."));

        Sector sector = sectorRepository.save((Sector.builder()
            .name(TEST_SECTOR_NAME)
            .description(TEST_SECTOR_DESCRIPTION)
            .state(SectorState.PUBLISHED)
            .lastModifier(user)
            .creator(user)
            .build()));
        sectorId = sector.getId();

        Post post = postRepository.save(Post.builder()
            .area(area)
            .creator(user)
            .postImages(null)
            .sector(sector)
            .writing(TEST_POST_WRITING)
            .build());
        postId = post.getId();

        assertThat(postRepository.findAll()).hasSize(1);
    }

    @DisplayName("랭킹 조회 - 성공")
    @Test
    void findRankingBy_Success() {
        RankingCriteria rankingCriteria = RankingCriteria.TOTAL;
        LocalDateTime startDate = rankingCriteria.getStartDate();
        LocalDateTime endDate = rankingCriteria.getEndDate();

        PageRequest pageRequest = new PageRequest(0, 10, null, null);
        Pageable pageable = PageableAssembler.assembleForRanking(pageRequest);

        Page<Post> postsPage = postRepository.findRankingBy(pageable, startDate, endDate);

        assertThat(postsPage.getContent()).hasSize(1);
        assertThat(postsPage.getTotalPages()).isEqualTo(1);
        assertThat(postsPage.getTotalElements()).isEqualTo(1);
    }

    @DisplayName("랭킹 조회 - PAGING 초과시 빈 PAGE 주는지 확인")
    @Test
    void findRankingBy_OverPage_ReturnEmptyPage() {
        RankingCriteria rankingCriteria = RankingCriteria.TOTAL;
        LocalDateTime startDate = rankingCriteria.getStartDate();
        LocalDateTime endDate = rankingCriteria.getEndDate();

        PageRequest pageRequest = new PageRequest(1000, 10, null, null);
        Pageable pageable = PageableAssembler.assembleForRanking(pageRequest);

        Page<Post> postsPage = postRepository.findRankingBy(pageable, startDate, endDate);

        assertThat(postsPage.getContent()).hasSize(0);
        assertThat(postsPage.getTotalPages()).isEqualTo(1);
        assertThat(postsPage.getTotalElements()).isEqualTo(1);
    }

    @DisplayName("랭킹 단일 지역 조회 - 성공")
    @Test
    void findRankingByAreaId_Success() {
        RankingCriteria rankingCriteria = RankingCriteria.TOTAL;
        LocalDateTime startDate = rankingCriteria.getStartDate();
        LocalDateTime endDate = rankingCriteria.getEndDate();

        PageRequest pageRequest = new PageRequest(0, 10, null, null);
        Pageable pageable = PageableAssembler.assembleForRanking(pageRequest);

        Page<Post> postsPage
            = postRepository.findRankingByAreaId(pageable, TEST_AREA_ID, startDate, endDate);

        assertThat(postsPage.getContent()).hasSize(1);
        assertThat(postsPage.getTotalPages()).isEqualTo(1);
        assertThat(postsPage.getTotalElements()).isEqualTo(1);
    }

    @DisplayName("랭킹 단일 지역 조회 - PAGING 초과시 빈 PAGE 주는지 확인")
    @Test
    void findRankingByAreaId_OverPage_ReturnEmptyPage() {
        RankingCriteria rankingCriteria = RankingCriteria.TOTAL;
        LocalDateTime startDate = rankingCriteria.getStartDate();
        LocalDateTime endDate = rankingCriteria.getEndDate();

        PageRequest pageRequest = new PageRequest(1000, 10, null, null);
        Pageable pageable = PageableAssembler.assembleForRanking(pageRequest);

        Page<Post> postsPage
            = postRepository.findRankingByAreaId(pageable, TEST_AREA_ID, startDate, endDate);

        assertThat(postsPage.getContent()).hasSize(0);
        assertThat(postsPage.getTotalPages()).isEqualTo(1);
        assertThat(postsPage.getTotalElements()).isEqualTo(1);
    }

    @DisplayName("랭킹 다중 부문 조회 - 성공")
    @Test
    void findRankingBySectorIds_Success() {
        RankingCriteria rankingCriteria = RankingCriteria.TOTAL;
        LocalDateTime startDate = rankingCriteria.getStartDate();
        LocalDateTime endDate = rankingCriteria.getEndDate();

        PageRequest pageRequest = new PageRequest(0, 10, null, null);
        Pageable pageable = PageableAssembler.assembleForRanking(pageRequest);

        List<Long> sectorIds = Collections.singletonList(sectorId);

        Page<Post> postsPage = postRepository
            .findRankingBySectorIds(pageable, sectorIds, startDate, endDate);

        assertThat(postsPage.getContent()).hasSize(1);
        assertThat(postsPage.getTotalPages()).isEqualTo(1);
        assertThat(postsPage.getTotalElements()).isEqualTo(1);
    }

    @DisplayName("랭킹 다중 부문 조회 - PAGING 초과시 빈 PAGE 주는지 확인")
    @Test
    void findRankingBySectorIds_OverPage_ReturnEmptyPage() {
        RankingCriteria rankingCriteria = RankingCriteria.TOTAL;
        LocalDateTime startDate = rankingCriteria.getStartDate();
        LocalDateTime endDate = rankingCriteria.getEndDate();

        PageRequest pageRequest = new PageRequest(1000, 10, null, null);
        Pageable pageable = PageableAssembler.assembleForRanking(pageRequest);

        List<Long> sectorIds = Collections.singletonList(sectorId);

        Page<Post> postsPage = postRepository
            .findRankingBySectorIds(pageable, sectorIds, startDate, endDate);

        assertThat(postsPage.getContent()).hasSize(0);
        assertThat(postsPage.getTotalPages()).isEqualTo(1);
        assertThat(postsPage.getTotalElements()).isEqualTo(1);
    }

    @DisplayName("랭킹 단일 지역 다중 부문 조회 - 성공")
    @Test
    void findRankingByAreaIdAndSectorIds_Success() {
        RankingCriteria rankingCriteria = RankingCriteria.TOTAL;
        LocalDateTime startDate = rankingCriteria.getStartDate();
        LocalDateTime endDate = rankingCriteria.getEndDate();

        PageRequest pageRequest = new PageRequest(0, 10, null, null);
        Pageable pageable = PageableAssembler.assembleForRanking(pageRequest);

        List<Long> sectorIds = Collections.singletonList(sectorId);

        Page<Post> postsPage = postRepository
            .findRankingByAreaIdAndSectorIds(pageable, TEST_AREA_ID, sectorIds, startDate, endDate);

        assertThat(postsPage.getContent()).hasSize(1);
        assertThat(postsPage.getTotalPages()).isEqualTo(1);
        assertThat(postsPage.getTotalElements()).isEqualTo(1);
    }

    @DisplayName("랭킹 단일 지역 다중 부문 조회 - PAGING 초과시 빈 PAGE 주는지 확인")
    @Test
    void findRankingByAreaIdAndSectorIds_OverPage_ReturnEmptyPage() {
        RankingCriteria rankingCriteria = RankingCriteria.TOTAL;
        LocalDateTime startDate = rankingCriteria.getStartDate();
        LocalDateTime endDate = rankingCriteria.getEndDate();

        PageRequest pageRequest = new PageRequest(1000, 10, null, null);
        Pageable pageable = PageableAssembler.assembleForRanking(pageRequest);

        List<Long> sectorIds = Collections.singletonList(sectorId);

        Page<Post> postsPage = postRepository
            .findRankingByAreaIdAndSectorIds(pageable, TEST_AREA_ID, sectorIds, startDate, endDate);

        assertThat(postsPage.getContent()).hasSize(0);
        assertThat(postsPage.getTotalPages()).isEqualTo(1);
        assertThat(postsPage.getTotalElements()).isEqualTo(1);
    }
}
