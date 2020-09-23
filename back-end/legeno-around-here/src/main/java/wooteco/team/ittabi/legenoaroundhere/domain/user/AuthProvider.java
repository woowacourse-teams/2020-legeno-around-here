package wooteco.team.ittabi.legenoaroundhere.domain.user;

import java.util.Arrays;

public enum AuthProvider {
    LOCAL,
    GOOGLE,
    GITHUB;

    public static AuthProvider of(String provider) {
        return Arrays.stream(AuthProvider.values())
            .filter(authProvider -> authProvider.name().equalsIgnoreCase(provider))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("해당 하는 AuthProvider가 없습니다."));
    }

    public boolean isSame(String provider) {
        return this.name().equalsIgnoreCase(provider);
    }
}
