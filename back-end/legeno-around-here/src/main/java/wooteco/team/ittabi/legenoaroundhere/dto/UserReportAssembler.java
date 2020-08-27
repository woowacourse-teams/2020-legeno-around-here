package wooteco.team.ittabi.legenoaroundhere.dto;

import java.util.Objects;
import wooteco.team.ittabi.legenoaroundhere.domain.report.UserReport;
import wooteco.team.ittabi.legenoaroundhere.domain.report.UserSnapshot;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.domain.user.UserImage;

public class UserReportAssembler {

    public static UserReport assemble(ReportCreateRequest reportCreateRequest, User reporter,
        User user) {
        return UserReport.builder()
            .reportWriting(reportCreateRequest.getWriting())
            .reporter(reporter)
            .userSnapshot(makeUserSnapshot(user))
            .build();
    }

    private static UserSnapshot makeUserSnapshot(User user) {
        return UserSnapshot.builder()
            .userNickname(user.getNickname())
            .userImageUrl(makeUserSnapshotImage(user.getImage()))
            .build();
    }

    private static String makeUserSnapshotImage(UserImage user) {
        if (Objects.isNull(user)) {
            return null;
        }
        return user.getUrl();
    }
}
