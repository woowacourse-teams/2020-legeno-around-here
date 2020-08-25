package wooteco.team.ittabi.legenoaroundhere.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.post.PostReport;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class PostReportResponse {

    private Long id;
    private String writing;
    private UserResponse creator;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public PostReportResponse(String writing) {
        this.writing = writing;
    }

    public static PostReportResponse of(PostReport postReport) {
        return PostReportResponse.builder()
            .id(postReport.getId())
            .writing(postReport.getWriting())
            .creator(UserResponse.from(postReport.getCreator()))
            .createdAt(postReport.getCreatedAt())
            .modifiedAt(postReport.getModifiedAt())
            .build();
    }
}
