package wooteco.team.ittabi.legenoaroundhere.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.report.CommentReport;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class CommentReportResponse {

    private Long id;
    private String reportWriting;
    private String commentWriting;
    private UserSimpleResponse reporter;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static CommentReportResponse of(CommentReport commentReport) {
        return CommentReportResponse.builder()
            .id(commentReport.getId())
            .reportWriting(commentReport.getReportWriting())
            .commentWriting(commentReport.getCommentWriting())
            .reporter(UserSimpleResponse.from(commentReport.getReporter()))
            .createdAt(commentReport.getCreatedAt())
            .modifiedAt(commentReport.getModifiedAt())
            .build();
    }
}
