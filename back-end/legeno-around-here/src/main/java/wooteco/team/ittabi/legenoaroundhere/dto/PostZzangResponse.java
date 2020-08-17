package wooteco.team.ittabi.legenoaroundhere.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class PostZzangResponse {

    private int count;
    private boolean activated;

    public static PostZzangResponse of(int likeCount, boolean activated) {
        return new PostZzangResponse(likeCount, activated);
    }
}
