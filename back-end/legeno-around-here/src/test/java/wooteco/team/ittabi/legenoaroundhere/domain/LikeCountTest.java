package wooteco.team.ittabi.legenoaroundhere.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.team.ittabi.legenoaroundhere.domain.post.like.LikeCount;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

public class LikeCountTest {

    public static final int DEFAULT_LIKE_COUNT = 1;

    @DisplayName("좋아요 수 증가")
    @Test
    void addLikeCount() {
        LikeCount likeCount = new LikeCount(DEFAULT_LIKE_COUNT);
        LikeCount expectedLikeCount = likeCount.plusLikeCount();

        assertThat(expectedLikeCount.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT + 1);
    }

    @DisplayName("좋아요 수 감소")
    @Test
    void minusLikeCount() {
        LikeCount likeCount = new LikeCount(DEFAULT_LIKE_COUNT);
        LikeCount expectedLikeCount = likeCount.minusLikeCount();

        assertThat(expectedLikeCount.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT - 1);
    }

    @DisplayName("좋아요 수가 0 이하")
    @Test
    void minusLikeCount_LessThanZero_ThrownException() {
        LikeCount likeCount = new LikeCount(DEFAULT_LIKE_COUNT);

        assertThatThrownBy(() -> likeCount.minusLikeCount().minusLikeCount())
            .isInstanceOf(WrongUserInputException.class);
    }
}
