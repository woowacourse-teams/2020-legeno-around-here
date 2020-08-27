package wooteco.team.ittabi.legenoaroundhere.domain.report;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class CommentReport extends ReportEntity {

    @Embedded
    private CommentSnapshot commentSnapshot;

    @Builder
    public CommentReport(String reportWriting, User reporter, CommentSnapshot commentSnapshot) {
        super(reportWriting, reporter);
        this.commentSnapshot = commentSnapshot;
    }

    public String getCommentWriting() {
        return this.commentSnapshot.getCommentWriting();
    }
}
