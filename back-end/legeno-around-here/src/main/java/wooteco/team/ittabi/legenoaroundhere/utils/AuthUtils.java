package wooteco.team.ittabi.legenoaroundhere.utils;

import java.util.concurrent.ThreadLocalRandom;

public class AuthUtils {

    private static final int AUTH_NUMBER_MIN = 100_000_000;
    private static final int AUTH_NUMBER_RANGE = 900_000_000;

    public static int makeRandomAuthNumber() {
        return AUTH_NUMBER_MIN + ThreadLocalRandom.current().nextInt(AUTH_NUMBER_RANGE);
    }
}
