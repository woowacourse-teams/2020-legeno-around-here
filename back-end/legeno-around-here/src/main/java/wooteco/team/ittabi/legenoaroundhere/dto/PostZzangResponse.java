package wooteco.team.ittabi.legenoaroundhere.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.post.zzang.ZzangState;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class PostZzangResponse {

    private int postZzangCount;
    private String zzangState;

    public static PostZzangResponse of(int likeCount, ZzangState zzangState) {
        return new PostZzangResponse(likeCount, zzangState.name());
    }
}
