package wooteco.team.ittabi.legenoaroundhere.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.report.PostReport;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class PostReportResponse {

    private Long id;
    private String reportWriting;
    private String postWriting;
    private List<String> postImageUrls;
    private UserSimpleResponse reporter;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static PostReportResponse of(PostReport postReport) {
        return PostReportResponse.builder()
            .id(postReport.getId())
            .reportWriting(postReport.getReportWriting())
            .postWriting(postReport.getPostWriting())
            .postImageUrls(postReport.getPostImageUrls())
            .reporter(UserSimpleResponse.from(postReport.getReporter()))
            .createdAt(postReport.getCreatedAt())
            .modifiedAt(postReport.getModifiedAt())
            .build();
    }
}
