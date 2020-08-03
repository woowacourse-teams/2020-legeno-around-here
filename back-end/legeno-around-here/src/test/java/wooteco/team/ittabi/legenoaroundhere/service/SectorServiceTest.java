package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_ADMIN_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_ADMIN_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_SECTOR_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_SECTOR_NAME;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotUniqueException;
import wooteco.team.ittabi.legenoaroundhere.infra.JwtTokenGenerator;
import wooteco.team.ittabi.legenoaroundhere.repository.UserRepository;

@DataJpaTest
@Import({SectorService.class, UserService.class, JwtTokenGenerator.class})
class SectorServiceTest {

    private static final long INVALID_ID = -1L;
    @Autowired
    SectorService sectorService;

    @Autowired
    UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User admin;

    @BeforeEach
    void setUp() {
        admin = createAdmin(TEST_ADMIN_EMAIL, TEST_ADMIN_NICKNAME, TEST_ADMIN_EMAIL);
        setAuthentication(admin);
    }

    private User createAdmin(String email, String nickname, String password) {
        UserCreateRequest userCreateRequest = new UserCreateRequest(email, nickname, password);
        Long userId = userService.createAdmin(userCreateRequest);

        return userRepository.findById(userId)
            .orElseThrow(() -> new NotExistsException("해당하는 사용자가 존재하지 않습니다."));
    }

    private void setAuthentication(User user) {
        UserDetails userDetails = userService.loadUserByUsername(user.getEmailByString());
        Authentication authToken = new UsernamePasswordAuthenticationToken(
            user, "TestCredentials", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    @DisplayName("Sector 생성 - 성공")
    @Test
    void createSector_NotDuplicateName_Success() {
        SectorRequest sectorRequest = new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION);
        SectorResponse sectorResponse = sectorService.createSector(sectorRequest);

        assertThat(sectorResponse.getId()).isNotNull();
        assertThat(sectorResponse.getName()).isEqualTo(TEST_SECTOR_NAME);
        assertThat(sectorResponse.getDescription()).isEqualTo(TEST_SECTOR_DESCRIPTION);
        assertThat(sectorResponse.getUser()).isEqualTo(UserResponse.from(admin));
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
    void findSector_HasId_Success() {
        SectorRequest sectorRequest = new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION);
        SectorResponse createdSector = sectorService.createSector(sectorRequest);
        Long createdSectorId = createdSector.getId();

        SectorResponse foundSector = sectorService.findSector(createdSectorId);
        assertThat(foundSector).isEqualTo(createdSector);
    }

    @DisplayName("Sector 조회 - 예외 발생, ID가 없는 경우")
    @Test
    void findSector_HasNotId_ThrowException() {
        assertThatThrownBy(() -> sectorService.findSector(INVALID_ID))
            .isInstanceOf(NotExistsException.class);
    }
}