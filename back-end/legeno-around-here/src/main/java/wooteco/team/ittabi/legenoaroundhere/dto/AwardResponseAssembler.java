package wooteco.team.ittabi.legenoaroundhere.dto;

import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import wooteco.team.ittabi.legenoaroundhere.domain.award.PopularityPostCreatorAward;
import wooteco.team.ittabi.legenoaroundhere.domain.award.SectorCreatorAward;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AwardResponseAssembler {

    private static final DateTimeFormatter DATE_FORMAT_YMD
        = DateTimeFormatter.ofPattern("yyyy.MM.dd.");

    public static AwardResponse assemble(PopularityPostCreatorAward popularityPostCreatorAward) {
        Post post = popularityPostCreatorAward.getPost();

        return AwardResponse.builder()
            .name(popularityPostCreatorAward.getName())
            .period(popularityPostCreatorAward.getPeriodStart().format(DATE_FORMAT_YMD) + " ~ " +
                popularityPostCreatorAward.getPeriodEnd().format(DATE_FORMAT_YMD))
            .location("/posts/" + post.getId())
            .build();
    }

    public static AwardResponse assemble(SectorCreatorAward sectorCreatorAward) {
        Sector sector = sectorCreatorAward.getSector();

        return AwardResponse.builder()
            .name(sectorCreatorAward.getName())
            .period(sectorCreatorAward.getCreatedAt().format(DATE_FORMAT_YMD))
            .location("/sectors/" + sector.getId())
            .build();
    }
}
