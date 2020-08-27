package wooteco.team.ittabi.legenoaroundhere.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.report.UserReport;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class UserReportResponse {

    private Long id;
    private String reportWriting;
    private String userNickname;
    private String userImageUrl;
    private UserSimpleResponse reporter;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static UserReportResponse of(UserReport userReport) {
        return UserReportResponse.builder()
            .id(userReport.getId())
            .reportWriting(userReport.getReportWriting())
            .userNickname(userReport.getUserNickname())
            .userImageUrl(userReport.getUserImageUrl())
            .reporter(UserSimpleResponse.from(userReport.getReporter()))
            .createdAt(userReport.getCreatedAt())
            .modifiedAt(userReport.getModifiedAt())
            .build();
    }
}
