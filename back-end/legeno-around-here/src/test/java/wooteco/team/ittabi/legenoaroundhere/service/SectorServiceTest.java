package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_ADMIN_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_ADMIN_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_SECTOR_ANOTHER_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_SECTOR_ANOTHER_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_SECTOR_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_SECTOR_NAME;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import wooteco.team.ittabi.legenoaroundhere.config.AuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.SectorState;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.AdminSectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorDetailResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorUpdateStateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotUniqueException;
import wooteco.team.ittabi.legenoaroundhere.infra.JwtTokenGenerator;
import wooteco.team.ittabi.legenoaroundhere.repository.UserRepository;

@DataJpaTest
@Import({SectorService.class, UserService.class, JwtTokenGenerator.class,
    AuthenticationFacade.class})
class SectorServiceTest {

    private static final long INVALID_ID = -1L;
    @Autowired
    SectorService sectorService;

    @Autowired
    UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    private User admin;

    @BeforeEach
    void setUp() {
        admin = createAdmin();
        setAuthentication(admin);
    }

    private User createAdmin() {
        UserRequest userRequest
            = new UserRequest(TEST_ADMIN_EMAIL, TEST_ADMIN_NICKNAME, TEST_ADMIN_EMAIL);
        Long userId = userService.createAdmin(userRequest);

        return userRepository.findById(userId)
            .orElseThrow(() -> new NotExistsException("해당하는 사용자가 존재하지 않습니다."));
    }

    private void setAuthentication(User user) {
        UserDetails userDetails = userService.loadUserByUsername(user.getEmailByString());
        Authentication authToken = new UsernamePasswordAuthenticationToken(
            user, "TestCredentials", userDetails.getAuthorities());
        authenticationFacade.setAuthentication(authToken);
    }

    @DisplayName("Sector 생성 - 성공")
    @Test
    void createSector_Success() {
        SectorRequest sectorRequest = new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION);
        SectorResponse sectorResponse = sectorService.createSector(sectorRequest);

        assertThat(sectorResponse.getId()).isNotNull();
        assertThat(sectorResponse.getName()).isEqualToIgnoringCase(TEST_SECTOR_NAME);
        assertThat(sectorResponse.getDescription()).isEqualTo(TEST_SECTOR_DESCRIPTION);
        assertThat(sectorResponse.getCreator()).isEqualTo(UserResponse.from(admin));
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
        assertThatThrownBy(() -> sectorService.findAvailableSector(INVALID_ID))
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

    @DisplayName("사용중인 Sector 전체 조회 - 성공")
    @Test
    void findAllUsedSector_Success() {
        SectorResponse sector
            = sectorService
            .createSector(new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION));
        SectorResponse anotherSector = sectorService.createSector(
            new SectorRequest(TEST_SECTOR_ANOTHER_NAME, TEST_SECTOR_ANOTHER_DESCRIPTION));
        sectorService.deleteSector(sector.getId());

        Page<SectorResponse> sectors = sectorService.findAllAvailableSector(Pageable.unpaged());
        assertThat(sectors).doesNotContain(sector);
        assertThat(sectors).contains(anotherSector);
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

        assertThatThrownBy(() -> sectorService.updateSector(INVALID_ID, sectorRequest))
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
        sectorService.deleteSector(sectorId);

        AdminSectorResponse sector = sectorService.findSector(sectorId);
        assertThat(sector.getId()).isEqualTo(sectorId);
        assertThat(sector.getName()).isEqualToIgnoringCase(TEST_SECTOR_NAME);
        assertThat(sector.getDescription()).isEqualTo(TEST_SECTOR_DESCRIPTION);
        assertThat(sector.getState()).isEqualTo(SectorState.DELETED.getName());
    }

    @DisplayName("Sector 삭제 - 예외 발생, ID가 없는 경우")
    @Test
    void deleteSector_HasNotId_ThrownException() {
        assertThatThrownBy(() -> sectorService.deleteSector(INVALID_ID))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("Sector 조회 - 예외 발생, ID가 없는 경우")
    @Test
    void findSector_HasNotId_ThrowException() {
        assertThatThrownBy(() -> sectorService.findSector(INVALID_ID))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("Sector 전체 조회 - 성공")
    @Test
    void findAllSector() {
        SectorResponse sector
            = sectorService
            .createSector(new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION));
        SectorResponse anotherSector = sectorService.createSector(
            new SectorRequest(TEST_SECTOR_ANOTHER_NAME, TEST_SECTOR_ANOTHER_DESCRIPTION));
        sectorService.deleteSector(sector.getId());

        AdminSectorResponse expectedSector = sectorService.findSector(sector.getId());
        AdminSectorResponse expectedAnotherSector
            = sectorService.findSector(anotherSector.getId());

        Page<AdminSectorResponse> sectors = sectorService.findAllSector(Pageable.unpaged());
        assertThat(sectors).contains(expectedSector);
        assertThat(sectors).contains(expectedAnotherSector);
    }

    @DisplayName("승인 신청 상태의 Sector 생성 - 성공")
    @Test
    void createPendingSector_Success() {
        SectorRequest sectorRequest = new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION);
        SectorResponse sector = sectorService.createPendingSector(sectorRequest);

        assertThat(sector.getId()).isNotNull();
        assertThat(sector.getName()).isEqualToIgnoringCase(sectorRequest.getName());
        assertThat(sector.getDescription()).isEqualTo(sectorRequest.getDescription());
        assertThat(sector.getCreator()).isEqualTo(UserResponse.from(admin));
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
    void findAllMySector_Success() {
        List<Long> sectorIds = new ArrayList<>();
        SectorRequest sectorRequest = new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION);
        sectorIds.add(sectorService.createPendingSector(sectorRequest).getId());
        SectorRequest sectorAnotherRequest
            = new SectorRequest(TEST_SECTOR_ANOTHER_NAME, TEST_SECTOR_DESCRIPTION);
        sectorIds.add(sectorService.createPendingSector(sectorAnotherRequest).getId());

        Page<SectorDetailResponse> sectors = sectorService.findAllMySector(Pageable.unpaged());
        assertThat(sectors.stream()
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
}