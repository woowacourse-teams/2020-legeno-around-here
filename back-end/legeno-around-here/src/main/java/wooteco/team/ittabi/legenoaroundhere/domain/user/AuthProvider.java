package wooteco.team.ittabi.legenoaroundhere.domain.user;

import java.util.Arrays;

public enum AuthProvider {
    LOCAL("local"),
    GOOGLE("google"),
    GITHUB("github");

    private final String name;

    AuthProvider(String name) {
        this.name = name;
    }

    public static AuthProvider of(String provider) {
        return Arrays.stream(AuthProvider.values())
            .filter(authProvider -> authProvider.name.equalsIgnoreCase(provider))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("해당 하는 AuthProvider가 없습니다."));
    }

    public boolean isSame(String provider) {
        return this.name.equalsIgnoreCase(provider);
    }
}
