package wooteco.team.ittabi.legenoaroundhere.dto;

import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.award.PopularPostAward;
import wooteco.team.ittabi.legenoaroundhere.domain.award.SectorCreatorAward;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.util.UrlPathConstants;

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

    public static AwardResponse of(PopularPostAward popularPostAward) {
        Post post = popularPostAward.getPost();

        String date = popularPostAward.getStartDate().format(DATE_FORMAT_YMD)
            + " ~ " + popularPostAward.getEndDate().format(DATE_FORMAT_YMD);

        return AwardResponse.builder()
            .name(popularPostAward.getName())
            .date(date)
            .location(UrlPathConstants.POSTS_PATH_WITH_SLASH + post.getId())
            .build();
    }

    public static AwardResponse of(SectorCreatorAward sectorCreatorAward) {
        Sector sector = sectorCreatorAward.getSector();

        return AwardResponse.builder()
            .name(sectorCreatorAward.getName())
            .date(sectorCreatorAward.getDate().format(DATE_FORMAT_YMD))
            .location(UrlPathConstants.SECTORS_PATH_WITH_SLASH + sector.getId())
            .build();
    }
}
