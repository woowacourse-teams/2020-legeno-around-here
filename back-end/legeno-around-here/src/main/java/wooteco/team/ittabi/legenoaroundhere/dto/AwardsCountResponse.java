package wooteco.team.ittabi.legenoaroundhere.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class AwardsCountResponse {

    private static final int INITIAL_AWARD_COUNT = 0;
    private static final AwardsCountResponse INITIAL_AWARDS_COUNT_RESPONSE
        = new AwardsCountResponse(INITIAL_AWARD_COUNT, INITIAL_AWARD_COUNT, INITIAL_AWARD_COUNT);

    private int topOne;
    private int topThree;
    private int sector;

    public static AwardsCountResponse of(User awardee) {
        return AwardsCountResponse.builder()
            .topOne(awardee.countTopPopularityPostCreatorAwardsBy(1))
            .topThree(awardee.countTopPopularityPostCreatorAwardsBy(3))
            .sector(awardee.countSectorCreatorAwards())
            .build();
    }

    public static AwardsCountResponse ofInitialValue() {
        return INITIAL_AWARDS_COUNT_RESPONSE;
    }
}
