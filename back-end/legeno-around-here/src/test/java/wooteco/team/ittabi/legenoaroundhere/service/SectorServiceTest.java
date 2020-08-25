package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_POST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_ANOTHER_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_ANOTHER_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_INVALID_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_ID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.SectorState;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.AdminSectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorDetailResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorUpdateStateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserSimpleResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotUniqueException;
import wooteco.team.ittabi.legenoaroundhere.repository.UserRepository;

class SectorServiceTest extends ServiceTest {

    @Autowired
    private SectorService sectorService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    private User admin;

    @BeforeEach
    void setUp() {
        admin = userRepository.findById(TEST_ADMIN_ID)
            .orElseThrow(() -> new NotExistsException(TEST_ADMIN_ID + "에 해당하는 User가 없습니다."));
        setAuthentication(admin);
    }

    @DisplayName("Sector 생성 - 성공")
    @Test
    void createSector_Success() {
        SectorRequest sectorRequest = new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION);
        SectorResponse sectorResponse = sectorService.createSector(sectorRequest);

        assertThat(sectorResponse.getId()).isNotNull();
        assertThat(sectorResponse.getName()).isEqualToIgnoringCase(TEST_SECTOR_NAME);
        assertThat(sectorResponse.getDescription()).isEqualTo(TEST_SECTOR_DESCRIPTION);
        assertThat(sectorResponse.getCreator()).isEqualTo(UserSimpleResponse.from(admin));
    }

    @DisplayName("Sector 생성 - 예외 발생, 동일 이름의 부문이 존재")
    @Test
    void createSector_DuplicateName_ThrownException() {
        SectorRequest sectorRequest = new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION);
        sectorService.createSector(sectorRequest);

        assertThatThrownBy(() -> sectorService.createSector(sectorRequest))
            .isInstanceOf(NotUniqueException.class);
    }

    @DisplayName("Sector 조회 - 성공, ID가 있는 경우")
    @Test
    void findSector_HasUsedId_Success() {
        SectorRequest sectorRequest = new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION);
        SectorResponse createdSector = sectorService.createSector(sectorRequest);
        Long createdSectorId = createdSector.getId();

        SectorResponse foundSector = sectorService.findAvailableSector(createdSectorId);
        assertThat(foundSector).isEqualTo(createdSector);
    }

    @DisplayName("사용중인 Sector 조회 - 예외 발생, ID가 없는 경우")
    @Test
    void findUsedSector_HasNotId_ThrowException() {
        assertThatThrownBy(() -> sectorService.findAvailableSector(TEST_SECTOR_INVALID_ID))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("사용중인 Sector 조회 - 예외 발생, 사용중인 ID가 없는 경우")
    @Test
    void findUsedSector_HasNotUsedId_ThrowException() {
        SectorRequest sectorRequest = new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION);
        SectorResponse createdSector = sectorService.createSector(sectorRequest);
        Long sectorId = createdSector.getId();
        sectorService.deleteSector(sectorId);

        assertThatThrownBy(() -> sectorService.findAvailableSector(sectorId))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("사용중인 Sector 키워드 조회 - 성공")
    @Test
    void searchAvailableSectors_SearchKeyword_Success() {
        SectorResponse sector = sectorService
            .createSector(new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION));
        SectorResponse anotherSector = sectorService.createSector(
            new SectorRequest(TEST_SECTOR_ANOTHER_NAME, TEST_SECTOR_ANOTHER_DESCRIPTION));

        Page<SectorResponse> sectors = sectorService
            .searchAvailableSectors(Pageable.unpaged(), TEST_SECTOR_ANOTHER_NAME.substring(3));

        assertThat(sectors.getContent()).doesNotContain(sector);
        assertThat(sectors.getContent()).contains(anotherSector);
    }

    @DisplayName("사용중인 Sector 전체 조회 - 성공")
    @Test
    void searchAvailableSectors_Success() {
        SectorResponse sector
            = sectorService
            .createSector(new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION));
        SectorResponse anotherSector = sectorService.createSector(
            new SectorRequest(TEST_SECTOR_ANOTHER_NAME, TEST_SECTOR_ANOTHER_DESCRIPTION));
        sectorService.deleteSector(sector.getId());

        Page<SectorResponse> sectors = sectorService.searchAvailableSectors(Pageable.unpaged(), "");
        assertThat(sectors.getContent()).doesNotContain(sector);
        assertThat(sectors.getContent()).contains(anotherSector);
    }

    @DisplayName("Sector 수정 - 성공, ID가 있는 경우")
    @Test
    void updateSector_HasId_Success() {
        Long id = sectorService
            .createSector(new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION))
            .getId();

        SectorRequest sectorRequest =
            new SectorRequest(TEST_SECTOR_ANOTHER_NAME, TEST_SECTOR_ANOTHER_DESCRIPTION);
        sectorService.updateSector(id, sectorRequest);

        SectorResponse updatedSector = sectorService.findAvailableSector(id);
        assertThat(updatedSector.getId()).isEqualTo(id);
        assertThat(updatedSector.getName()).isEqualToIgnoringCase(TEST_SECTOR_ANOTHER_NAME);
        assertThat(updatedSector.getDescription()).isEqualTo(TEST_SECTOR_ANOTHER_DESCRIPTION);
        assertThat(updatedSector.getCreator()).isNotNull();
    }

    @DisplayName("Sector 수정 - 예외 발생, ID가 없는 경우")
    @Test
    void updateSector_HasNotId_ThrownException() {
        SectorRequest sectorRequest = new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION);

        assertThatThrownBy(() -> sectorService.updateSector(TEST_SECTOR_INVALID_ID, sectorRequest))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("Sector 삭제 - 성공, ID가 있는 경우")
    @Test
    void deleteSector_HasId_Success() {
        Long id = sectorService
            .createSector(new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION))
            .getId();

        sectorService.deleteSector(id);

        assertThatThrownBy(() -> sectorService.findAvailableSector(id))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("Sector 조회 - 성공, ID가 있는 경우")
    @Test
    void findSector() {
        SectorResponse createdSector = sectorService
            .createSector(new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION));
        Long sectorId = createdSector.getId();

        AdminSectorResponse sector = sectorService.findSector(sectorId);
        assertThat(sector.getId()).isEqualTo(sectorId);
        assertThat(sector.getName()).isEqualToIgnoringCase(TEST_SECTOR_NAME);
        assertThat(sector.getDescription()).isEqualTo(TEST_SECTOR_DESCRIPTION);
        assertThat(sector.getState()).isEqualTo(SectorState.PUBLISHED.getName());
        assertThat(sector.getReason()).isNotNull();
    }

    @DisplayName("사용중인 Sector 조회 - 성공, 사용중이 아닌 ID가 있는 경우")
    @Test
    void findSector_HasNotUsedId_Success() {
        SectorRequest sectorRequest = new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION);
        SectorResponse createdSector = sectorService.createSector(sectorRequest);
        Long sectorId = createdSector.getId();
        sectorService.updateSectorState(sectorId, new SectorUpdateStateRequest("승인", "승인함"));

        AdminSectorResponse sector = sectorService.findSector(sectorId);
        assertThat(sector.getId()).isEqualTo(sectorId);
        assertThat(sector.getName()).isEqualToIgnoringCase(TEST_SECTOR_NAME);
        assertThat(sector.getDescription()).isEqualTo(TEST_SECTOR_DESCRIPTION);
        assertThat(sector.getState()).isEqualTo(SectorState.APPROVED.getName());
    }

    @DisplayName("Sector 삭제 - 예외 발생, ID가 없는 경우")
    @Test
    void deleteSector_HasNotId_ThrownException() {
        assertThatThrownBy(() -> sectorService.deleteSector(TEST_SECTOR_INVALID_ID))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("Sector 조회 - 예외 발생, ID가 없는 경우")
    @Test
    void findSector_HasNotId_ThrowException() {
        assertThatThrownBy(() -> sectorService.findSector(TEST_SECTOR_INVALID_ID))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("Sector 조회 - 예외 발생, 삭제한 ID인 경우")
    @Test
    void findSector_DeletedId_ThrowException() {
        SectorRequest sectorRequest = new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION);
        SectorResponse createdSector = sectorService.createSector(sectorRequest);
        Long sectorId = createdSector.getId();
        sectorService.deleteSector(sectorId);

        assertThatThrownBy(() -> sectorService.findSector(sectorId))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("Sector 전체 조회 - 성공")
    @Test
    void findAllSector() {
        SectorResponse sector = sectorService
            .createSector(new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION));
        SectorResponse anotherSector = sectorService.createSector(
            new SectorRequest(TEST_SECTOR_ANOTHER_NAME, TEST_SECTOR_ANOTHER_DESCRIPTION));

        AdminSectorResponse expectedSector = sectorService.findSector(sector.getId());
        AdminSectorResponse expectedAnotherSector = sectorService.findSector(anotherSector.getId());

        sectorService.deleteSector(sector.getId());
        assertThatThrownBy(() -> sectorService.findSector(sector.getId()))
            .isInstanceOf(NotExistsException.class);

        Page<AdminSectorResponse> sectors = sectorService.findAllSector(Pageable.unpaged());
        assertThat(sectors.getContent()).doesNotContain(expectedSector);
        assertThat(sectors.getContent()).contains(expectedAnotherSector);
    }

    @DisplayName("승인 신청 상태의 Sector 생성 - 성공")
    @Test
    void createPendingSector_Success() {
        SectorRequest sectorRequest = new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION);
        SectorResponse sector = sectorService.createPendingSector(sectorRequest);

        assertThat(sector.getId()).isNotNull();
        assertThat(sector.getName()).isEqualToIgnoringCase(sectorRequest.getName());
        assertThat(sector.getDescription()).isEqualTo(sectorRequest.getDescription());
        assertThat(sector.getCreator()).isEqualTo(UserSimpleResponse.from(admin));
    }

    @DisplayName("승인 신청 상태의 Sector 생성 - 예외 발생, 동일 이름의 Sector 존재")
    @Test
    void createPendingSector_DuplicateName_ThrownException() {
        SectorRequest sectorRequest = new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION);
        sectorService.createPendingSector(sectorRequest);

        assertThatThrownBy(() -> sectorService.createPendingSector(sectorRequest))
            .isInstanceOf(NotUniqueException.class);
    }

    @DisplayName("현재 사용자와 관련된 모든 부문 조회")
    @Test
    void findMySectors_Success() {
        List<Long> sectorIds = new ArrayList<>();
        SectorRequest sectorRequest = new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION);
        sectorIds.add(sectorService.createPendingSector(sectorRequest).getId());
        SectorRequest sectorAnotherRequest
            = new SectorRequest(TEST_SECTOR_ANOTHER_NAME, TEST_SECTOR_DESCRIPTION);
        sectorIds.add(sectorService.createPendingSector(sectorAnotherRequest).getId());

        Page<SectorDetailResponse> sectors = sectorService.findMySectors(Pageable.unpaged());
        assertThat(sectors.getContent().stream()
            .filter(sector -> sectorIds.contains(sector.getId()))
            .collect(Collectors.toList())).hasSize(2);
    }

    @DisplayName("SectorState 업데이트")
    @Test
    void updateSectorState_Success() {
        SectorRequest sectorRequest = new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION);
        Long sectorId = sectorService.createSector(sectorRequest).getId();

        String state = SectorState.APPROVED.getName();
        String reason = "이유";
        SectorUpdateStateRequest sectorUpdateStateRequest
            = new SectorUpdateStateRequest(state, reason);
        sectorService.updateSectorState(sectorId, sectorUpdateStateRequest);

        AdminSectorResponse sector = sectorService.findSector(sectorId);
        assertThat(sector.getState()).isEqualTo(state);
        assertThat(sector.getReason()).isEqualTo(reason);
    }

    @DisplayName("인기 부문 N개 조회 - 성공, 게시글 요청 개수만큼 조회되는지 확인")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void findBestSectors_CheckCount_Success(int count) {
        for (int i = 0; i < 10; i++) {
            SectorRequest sectorRequest
                = new SectorRequest(TEST_SECTOR_NAME + i, TEST_SECTOR_DESCRIPTION);
            sectorService.createSector(sectorRequest);
        }
        assertThat(sectorService.findBestSectors(count)).hasSize(count);
    }

    @DisplayName("인기 부문 조회 - 성공, 활성화된 게시글 순으로 조회되는지 확인")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void findBestSectors_CheckOrder_Success(int count) {
        List<Long> sectorIds = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SectorRequest sectorRequest
                = new SectorRequest(TEST_SECTOR_NAME + i, TEST_SECTOR_DESCRIPTION);
            long sectorId = sectorService.createSector(sectorRequest).getId();
            sectorIds.add(sectorId);
            for (int j = 0; j <= i; j++) {
                PostCreateRequest postCreateRequest
                    = new PostCreateRequest(TEST_POST_WRITING, null, TEST_AREA_ID, sectorId);
                postService.createPost(postCreateRequest);
            }
            for (int k = 0; k < 8 - (2 * i); k++) {
                PostCreateRequest postCreateRequest
                    = new PostCreateRequest(TEST_POST_WRITING, null, TEST_AREA_ID, sectorId);
                Long postId = postService.createPost(postCreateRequest).getId();
                postService.deletePost(postId);
            }
        }
        Collections.reverse(sectorIds);

        List<Long> bestSectorIds = sectorService.findBestSectors(count).stream()
            .map(SectorResponse::getId)
            .collect(Collectors.toList());
        assertThat(bestSectorIds).isEqualTo(sectorIds.subList(0, count));
    }
}
