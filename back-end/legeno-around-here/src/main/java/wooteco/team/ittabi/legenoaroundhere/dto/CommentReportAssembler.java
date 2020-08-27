package wooteco.team.ittabi.legenoaroundhere.dto;

import wooteco.team.ittabi.legenoaroundhere.domain.comment.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.report.CommentReport;
import wooteco.team.ittabi.legenoaroundhere.domain.report.CommentSnapshot;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

public class CommentReportAssembler {

    public static CommentReport assemble(ReportCreateRequest reportCreateRequest, User reporter,
        Comment comment) {
        return CommentReport.builder()
            .reportWriting(reportCreateRequest.getWriting())
            .reporter(reporter)
            .commentSnapshot(makeCommentSnapshot(comment))
            .build();
    }

    private static CommentSnapshot makeCommentSnapshot(Comment comment) {
        return CommentSnapshot.builder()
            .commentWriting(comment.getWriting())
            .build();
    }
}
