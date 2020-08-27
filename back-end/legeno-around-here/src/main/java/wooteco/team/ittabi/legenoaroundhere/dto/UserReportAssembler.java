package wooteco.team.ittabi.legenoaroundhere.dto;

import wooteco.team.ittabi.legenoaroundhere.domain.report.UserReport;
import wooteco.team.ittabi.legenoaroundhere.domain.report.UserSnapshot;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

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
            .userImageUrl(user.getImage().getUrl())
            .build();
    }
}
