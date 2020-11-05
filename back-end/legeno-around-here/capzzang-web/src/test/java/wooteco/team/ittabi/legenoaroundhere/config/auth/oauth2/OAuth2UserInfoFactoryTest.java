package wooteco.team.ittabi.legenoaroundhere.config.auth.oauth2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.team.ittabi.legenoaroundhere.config.auth.exception.OAuth2AuthenticationProcessingException;

class OAuth2UserInfoFactoryTest {

    @DisplayName("OAuth2UserInfo 생성 - 예외 발생, 올바르지 않은 RegistrationId")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"wrongRegistrationId"})
    void getOAuth2UserInfo_WrongRegistrationId_ThrownException(String registrationId) {
        assertThatThrownBy(
            () -> OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, new HashMap<>()))
            .isInstanceOf(OAuth2AuthenticationProcessingException.class);
    }

    @DisplayName("OAuth2UserInfo 생성 - 성공")
    @ParameterizedTest
    @ValueSource(strings = {"google"})
    void getOAuth2UserInfo_Success(String registrationId) {
        assertThat(OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, new HashMap<>()))
            .isInstanceOf(OAuth2UserInfo.class);
    }
}
