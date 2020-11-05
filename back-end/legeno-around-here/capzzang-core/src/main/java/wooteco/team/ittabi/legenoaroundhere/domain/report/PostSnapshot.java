package wooteco.team.ittabi.legenoaroundhere.domain.report;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
public class PostSnapshot {

    @Lob
    @Column(nullable = false)
    private String postWriting;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_report_id", foreignKey = @ForeignKey(name = "FK_POST_REPORT_POST_IMAGE_URLS_POST_REPORT"), nullable = false)
    private List<String> postImageUrls = new ArrayList<>();

    @Builder
    public PostSnapshot(String postWriting, List<String> postImageUrls) {
        this.postWriting = postWriting;
        this.postImageUrls = postImageUrls;
    }
}
