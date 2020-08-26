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
public class UserReport extends ReportEntity {

    @Embedded
    private UserSnapshot userSnapshot;

    @Builder
    public UserReport(String reportWriting, User reporter, UserSnapshot userSnapshot) {
        super(reportWriting, reporter);
        this.userSnapshot = userSnapshot;
    }

    public String getUserNickname() {
        return this.userSnapshot.getUserNickname();
    }

    public String getUserImageUrl() {
        return this.userSnapshot.getUserImageUrl();
    }
}
