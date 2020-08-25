package wooteco.team.ittabi.legenoaroundhere.dto;

import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.POSTS_PATH_WITH_SLASH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.SECTORS_PATH_WITH_SLASH;

import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.award.PopularityPostCreatorAward;
import wooteco.team.ittabi.legenoaroundhere.domain.award.SectorCreatorAward;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class AwardResponse {

    private static final DateTimeFormatter DATE_FORMAT_YMD
        = DateTimeFormatter.ofPattern("yyyy.MM.dd.");

    private String name;
    private String date;
    private String location;

    public static AwardResponse of(String name, String date, String location) {
        return AwardResponse.builder()
            .name(name)
            .date(date)
            .location(location)
            .build();
    }

    public static AwardResponse of(PopularityPostCreatorAward popularityPostCreatorAward) {
        Post post = popularityPostCreatorAward.getPost();

        String date = popularityPostCreatorAward.getStartDate().format(DATE_FORMAT_YMD)
            + " ~ " + popularityPostCreatorAward.getEndDate().format(DATE_FORMAT_YMD);

        return AwardResponse.builder()
            .name(popularityPostCreatorAward.getName())
            .date(date)
            .location(POSTS_PATH_WITH_SLASH + post.getId())
            .build();
    }

    public static AwardResponse of(SectorCreatorAward sectorCreatorAward) {
        Sector sector = sectorCreatorAward.getSector();

        return AwardResponse.builder()
            .name(sectorCreatorAward.getName())
            .date(sectorCreatorAward.getDate().format(DATE_FORMAT_YMD))
            .location(SECTORS_PATH_WITH_SLASH + sector.getId())
            .build();
    }
}
