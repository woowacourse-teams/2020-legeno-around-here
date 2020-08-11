package wooteco.team.ittabi.legenoaroundhere.domain;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@Getter
@EqualsAndHashCode
@ToString
public class LikeCount {

    private static final int MIN_LIKE_COUNT = 0;
    private static final int LIKE_COUNT_VALUE = 1;

    private long likeCount;

    public LikeCount(long likeCount) {
        validate(likeCount);
        this.likeCount = likeCount;
    }

    private void validate(long likeCount) {
        if (likeCount < MIN_LIKE_COUNT) {
            throw new WrongUserInputException("좋아요 수는 0 미만이 될 수 없습니다!");
        }
    }

    public LikeCount plusLikeCount() {
        return new LikeCount(this.likeCount + LIKE_COUNT_VALUE);
    }

    public LikeCount minusLikeCount() {
        return new LikeCount(this.likeCount - LIKE_COUNT_VALUE);
    }
}
