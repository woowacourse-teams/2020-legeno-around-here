package wooteco.team.ittabi.legenoaroundhere.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationContentMaker {

    public static String makeJoinNotificationContent(User user) {
        return user.getNickname() + "님, 가입을 진심으로 축하드립니다.";
    }
}
