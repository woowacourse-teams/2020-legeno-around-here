package wooteco.team.ittabi.legenoaroundhere.domain.sector;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_ADMIN_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_ADMIN_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_ADMIN_PASSWORD;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_PASSWORD;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_SECTOR_ANOTHER_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_SECTOR_ANOTHER_NAME;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_SECTOR_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_SECTOR_NAME;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Email;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Nickname;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Password;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

class SectorTest {

    private User user;
    private User admin;

    @BeforeEach
    void setUp() {
        user = makeUser(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        admin = makeUser(TEST_ADMIN_EMAIL, TEST_ADMIN_NICKNAME, TEST_ADMIN_PASSWORD);
    }

    private User makeUser(String email, String nickname, String password) {
        return User.builder()
            .email(new Email(email))
            .nickname(new Nickname(nickname))
            .password(new Password(password))
            .roles(new ArrayList<>())
            .post(new ArrayList<>())
            .build();
    }

    private Sector makeSector(String name, String description, User user, SectorState state) {
        return Sector.builder()
            .name(name)
            .description(description)
            .creator(user)
            .lastModifier(user)
            .state(state)
            .build();
    }

    @DisplayName("Sector의 내용(name, description, lastModifier)을 Update한다.")
    @Test
    void update() {
        Sector sector
            = makeSector(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION, user, SectorState.PUBLISHED);
        Sector expectedSector
            = makeSector(TEST_SECTOR_ANOTHER_NAME, TEST_SECTOR_ANOTHER_DESCRIPTION, admin,
            SectorState.DELETED);

        sector.update(expectedSector);

        assertThat(sector.getName()).isEqualTo(expectedSector.getName());
        assertThat(sector.getDescription()).isEqualTo(expectedSector.getDescription());
        assertThat(sector.getCreator().getEmail())
            .isNotEqualTo(expectedSector.getCreator().getEmail());
        assertThat(sector.getLastModifier().getEmail())
            .isEqualTo(expectedSector.getLastModifier().getEmail());
        assertThat(sector.getState()).isNotEqualTo(expectedSector.getState());
    }

    @DisplayName("Sector의 상태를 Update 한다.")
    @Test
    void setState() {
        Sector sector
            = makeSector(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION, user, SectorState.PUBLISHED);

        sector.setState(SectorState.DELETED);

        assertThat(sector.getState()).isEqualTo(SectorState.DELETED);
    }
}