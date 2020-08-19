package wooteco.team.ittabi.legenoaroundhere.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import wooteco.team.ittabi.legenoaroundhere.domain.award.PopularityPostCreatorAward;
import wooteco.team.ittabi.legenoaroundhere.domain.award.SectorCreatorAward;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;

public class AwardResponseAssembler {

    protected static final int DESCRIPTION_WORD_COUNT = 30;
    protected static final DateTimeFormatter DATE_FORMAT_YMD
        = DateTimeFormatter.ofPattern("yyyy.MM.dd.");

    public static AwardResponse assemble(PopularityPostCreatorAward popularityPostCreatorAward) {
        Post post = popularityPostCreatorAward.getPost();

        String awardName = makePopularPostCreatorAwardName(popularityPostCreatorAward, post);
        String period = makePeriod(popularityPostCreatorAward.getPeriodStart(),
            popularityPostCreatorAward.getPeriodEnd());

        return AwardResponse.builder()
            .name(awardName)
            .description(post.getWriting(DESCRIPTION_WORD_COUNT))
            .period(period)
            .date(popularityPostCreatorAward.getCreatedAt())
            .location("/posts/" + post.getId())
            .build();
    }

    private static String makePopularPostCreatorAwardName(
        PopularityPostCreatorAward popularityPostCreatorAward, Post post) {
        return popularityPostCreatorAward.getCycleName() + "의 "
            + post.getAreaLastDepthName() + " " + post.getSectorName()
            + " No." + popularityPostCreatorAward.getRanking();
    }

    private static String makePeriod(LocalDate periodStart, LocalDate periodEnd) {
        if (periodStart.equals(periodEnd)) {
            return periodEnd.format(DATE_FORMAT_YMD);
        }
        return periodStart.format(DATE_FORMAT_YMD) + " ~ " + periodEnd.format(DATE_FORMAT_YMD);
    }

    public static AwardResponse assemble(SectorCreatorAward sectorCreatorAward) {
        Sector sector = sectorCreatorAward.getSector();
        String awardName = sector.getName() + " 부문 창시자";

        return AwardResponse.builder()
            .name(awardName)
            .description(sector.getDescription(DESCRIPTION_WORD_COUNT))
            .date(sectorCreatorAward.getCreatedAt())
            .location("/sectors/" + sector.getId())
            .build();
    }
}
