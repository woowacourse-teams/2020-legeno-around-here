package wooteco.team.ittabi.legenoaroundhere.domain.report;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public abstract class ReportEntity extends BaseEntity {

    private static final int MAX_LENGTH = 2000;

    @Lob
    @Column(nullable = false)
    private String reportWriting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    public ReportEntity(String reportWriting, User reporter) {
        validateLength(reportWriting);
        this.reportWriting = reportWriting;
        this.reporter = reporter;
    }

    private void validateLength(String writing) {
        if (writing.length() > MAX_LENGTH) {
            throw new WrongUserInputException(MAX_LENGTH + "글자를 초과했습니다!");
        }
    }
}
