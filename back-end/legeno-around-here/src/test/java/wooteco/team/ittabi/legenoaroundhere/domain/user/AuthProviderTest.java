package wooteco.team.ittabi.legenoaroundhere.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AuthProviderTest {

    @DisplayName("정적 팩터리 메서드, AuthProvider 반환 - 존재하는 경우")
    @ParameterizedTest
    @ValueSource(strings = {"local", "LOCAL", "Local"})
    void of_ExistsProvider_ReturnAuthProvider(String local) {
        AuthProvider authProviderLocal = AuthProvider.of(local);
        assertThat(authProviderLocal).isEqualTo(AuthProvider.LOCAL);
    }

    @DisplayName("정적 팩터리 메서드, 예외 발생 - 존재하지 않는 경우")
    @Test
    void of_NotExistsProvider_ThrownException() {
        String wrongProvider = "wrongProvider";
        assertThatThrownBy(() -> AuthProvider.of(wrongProvider))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("같은 건지 비교, True - 같은 Provider")
    @ParameterizedTest
    @ValueSource(strings = {"local", "LOCAL", "Local"})
    void isSame_SameProvider_ReturnTrue(String local) {
        assertThat(AuthProvider.LOCAL.isSame(local)).isTrue();
    }

    @DisplayName("같은 건지 비교, False - 다른 Provider")
    @Test
    void isSame_DifferentProvider_ReturnFalse() {
        String wrongProvider = "wrongProvider";
        assertThat(AuthProvider.LOCAL.isSame(wrongProvider)).isFalse();
    }
}
