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

    private int top1;
    private int top3;
    private int sector;

    public static AwardsCountResponse of(User awardee) {
        return AwardsCountResponse.builder()
            .top1(awardee.countTopPopularityPostCreatorAwardsBy(1))
            .top3(awardee.countTopPopularityPostCreatorAwardsBy(3))
            .sector(awardee.countSectorCreatorAwards())
            .build();
    }
}
