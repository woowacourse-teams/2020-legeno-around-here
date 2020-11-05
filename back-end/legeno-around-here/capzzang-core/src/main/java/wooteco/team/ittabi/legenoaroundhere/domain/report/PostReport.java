package wooteco.team.ittabi.legenoaroundhere.domain.report;

import java.util.List;
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
public class PostReport extends ReportEntity {

    @Embedded
    private PostSnapshot postSnapshot;

    @Builder
    public PostReport(String reportWriting, User reporter, PostSnapshot postSnapshot) {
        super(reportWriting, reporter);
        this.postSnapshot = postSnapshot;
    }

    public String getPostWriting() {
        return this.postSnapshot.getPostWriting();
    }

    public List<String> getPostImageUrls() {
        return this.postSnapshot.getPostImageUrls();
    }
}
