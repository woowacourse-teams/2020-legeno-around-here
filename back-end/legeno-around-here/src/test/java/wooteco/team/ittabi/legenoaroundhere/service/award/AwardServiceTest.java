package wooteco.team.ittabi.legenoaroundhere.service.award;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_OTHER_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AUTH_NUMBER;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_EMPTY_IMAGES;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_POST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_ANOTHER_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_ANOTHER_REQUEST;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_REQUEST;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.domain.user.mailauth.MailAuth;
import wooteco.team.ittabi.legenoaroundhere.dto.AwardResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.repository.MailAuthRepository;
import wooteco.team.ittabi.legenoaroundhere.service.PostService;
import wooteco.team.ittabi.legenoaroundhere.service.SectorService;
import wooteco.team.ittabi.legenoaroundhere.service.ServiceTest;

class AwardServiceTest extends ServiceTest {

    private static final String TEST_PREFIX = "ranking_";
    private static final Long SEOUL_ID = 1L;
    private static final Long ID_IN_SEOUL = 2L;

    @MockBean
    private MailAuthRepository mailAuthRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private SectorService sectorService;

    @Autowired
    private AwardService awardService;

    private LocalDate setUpTime;
    private User user;
    private Long sectorId;
    private Long sectorOtherId;

    @BeforeEach
    void setUp() {
        setUpTime = LocalDate.now();
        MailAuth mailAuth = new MailAuth(TEST_USER_EMAIL, TEST_AUTH_NUMBER);
        when(mailAuthRepository.findByEmail(any())).thenReturn(java.util.Optional.of(mailAuth));

        user = createUser(TEST_PREFIX + TEST_USER_EMAIL,
            TEST_USER_NICKNAME,
            TEST_USER_PASSWORD);
        User userA = createUser(TEST_USER_EMAIL + "A", TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        User userB = createUser(TEST_USER_EMAIL + "B", TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        User userC = createUser(TEST_USER_EMAIL + "C", TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        User userD = createUser(TEST_USER_EMAIL + "D", TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        User userE = createUser(TEST_USER_EMAIL + "E", TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        setAuthentication(user);

        SectorResponse sector = sectorService.createSector(TEST_SECTOR_REQUEST);
        sectorId = sector.getId();
        sectorOtherId = sectorService.createSector(TEST_SECTOR_ANOTHER_REQUEST).getId();

        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, SEOUL_ID, sectorId);
        Long postIdA = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, SEOUL_ID, sectorId);
        Long postIdB = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, ID_IN_SEOUL, sectorId);
        Long postIdC = postService.createPost(postCreateRequest).getId();
        postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_OTHER_ID, sectorOtherId);
        Long postIdD = postService.createPost(postCreateRequest).getId();

        //PostA 짱 갯수 : 2, 서울시 sectorId(SECTORNAME) 부문 3위
        //PostB 짱 갯수 : 4, 서울시 sectorId(SECTORNAME) 부문 1위
        //PostC 짱 갯수 : 3, 서울시 종로구 sectorId(SECTORNAME) 부문 1위 + 서울시 sectorId 부문 2위
        //PostD 짱 갯수 : 1, 정자동 sectorOtherId(ANOTHERSECTORNAME) 부문 1위
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
    }

    @DisplayName("월간 인기 자랑글 상 일괄 수여")
    @Test
    void createMonthlyPopularPostAwards() {
        // 글 작성일로부터 한달 뒤
        LocalDateTime dayOfNextMonth = LocalDateTime.now().plusMonths(1L);

        awardService.createPopularPostAwards(dayOfNextMonth);

        List<AwardResponse> awardsOfUserA = awardService.findAwards(user.getId());
        assertThat(awardsOfUserA).hasSize(5);

        List<String> awardNames = awardsOfUserA.stream()
            .map(AwardResponse::getName)
            .collect(Collectors.toList());

        String awardNameDatePart = setUpTime.getYear() + "년 " + setUpTime.getMonthValue() + "월";

        assertThat(awardNames).contains(
            awardNameDatePart + " 서울특별시 " + TEST_SECTOR_NAME.toUpperCase() + " 부문 1위");
        assertThat(awardNames).contains(
            awardNameDatePart + " 서울특별시 " + TEST_SECTOR_NAME.toUpperCase() + " 부문 2위");
        assertThat(awardNames).contains(
            awardNameDatePart + " 서울특별시 " + TEST_SECTOR_NAME.toUpperCase() + " 부문 3위");
        assertThat(awardNames).contains(
            awardNameDatePart + " 종로구 " + TEST_SECTOR_NAME.toUpperCase() + " 부문 1위");
        assertThat(awardNames).contains(
            awardNameDatePart + " 정자동 " + TEST_SECTOR_ANOTHER_NAME.toUpperCase() + " 부문 1위");
    }
}
