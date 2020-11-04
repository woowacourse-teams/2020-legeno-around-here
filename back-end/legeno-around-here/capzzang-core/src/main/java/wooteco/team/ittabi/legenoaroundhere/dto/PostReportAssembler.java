package wooteco.team.ittabi.legenoaroundhere.dto;

import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.report.PostReport;
import wooteco.team.ittabi.legenoaroundhere.domain.report.PostSnapshot;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

public class PostReportAssembler {

    public static PostReport assemble(ReportCreateRequest reportCreateRequest,
        User reporter, Post post) {
        return PostReport.builder()
            .reportWriting(reportCreateRequest.getWriting())
            .postSnapshot(makePostSnapshot(post))
            .reporter(reporter)
            .build();
    }

    private static PostSnapshot makePostSnapshot(Post post) {
        return PostSnapshot.builder()
            .postWriting(post.getWriting())
            .postImageUrls(post.getPostImageUrls())
            .build();
    }
}
