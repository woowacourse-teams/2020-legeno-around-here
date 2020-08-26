package wooteco.team.ittabi.legenoaroundhere.dto;

import wooteco.team.ittabi.legenoaroundhere.domain.post.PostReport;
import wooteco.team.ittabi.legenoaroundhere.domain.post.PostSnapshot;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

public class PostReportAssembler {

    public static PostReport assemble(PostReportCreateRequest postReportCreateRequest,
        PostSnapshot postSnapshot, User user) {
        return PostReport.builder()
            .reportWriting(postReportCreateRequest.getWriting())
            .postSnapshot(postSnapshot)
            .reporter(user)
            .build();
    }
}
