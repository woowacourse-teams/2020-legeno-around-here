package wooteco.team.ittabi.legenoaroundhere.domain.post;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"reporter"})
public class PostReport extends BaseEntity {

    private static final int MAX_LENGTH = 2000;

    @Lob
    @Column(nullable = false)
    private String reportWriting;

    @Embedded
    private PostSnapshot postSnapshot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", foreignKey = @ForeignKey(name = "FK_POST_REPORT_REPORTER"), nullable = false)
    private User reporter;

    @Builder
    public PostReport(String reportWriting, PostSnapshot postSnapshot, User reporter) {
        validateLength(reportWriting);
        this.reportWriting = reportWriting;
        this.postSnapshot = postSnapshot;
        this.reporter = reporter;
    }

    private void validateLength(String writing) {
        if (writing.length() > MAX_LENGTH) {
            throw new WrongUserInputException(MAX_LENGTH + "글자를 초과했습니다!");
        }
    }

    public String getPostWriting() {
        return this.postSnapshot.getPostWriting();
    }

    public List<String> getPostImageUrls() {
        return this.postSnapshot.getPostImageUrls();
    }
}
