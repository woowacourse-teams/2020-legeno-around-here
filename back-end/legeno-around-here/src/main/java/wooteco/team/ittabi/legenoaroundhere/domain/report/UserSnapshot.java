package wooteco.team.ittabi.legenoaroundhere.domain.report;

import javax.persistence.Column;
import javax.persistence.Embeddable;
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
public class UserSnapshot {

    @Lob
    @Column(nullable = false)
    private String userNickname;

    private String userImageUrl;

    @Builder
    public UserSnapshot(String userNickname, String userImageUrl) {
        this.userNickname = userNickname;
        this.userImageUrl = userImageUrl;
    }
}
