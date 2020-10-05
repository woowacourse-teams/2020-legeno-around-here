package wooteco.team.ittabi.legenoaroundhere.domain.util;

import java.time.LocalDate;
import java.time.Month;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.ranking.RankingCriteria;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AwardNameMaker {

    private static final String SECTOR_CREATOR_AWARD_NAME_SUFFIX = " 부문 창시자";

    public static String makeSectorCreatorAwardName(Sector sector) {
        return sector.getName() + SECTOR_CREATOR_AWARD_NAME_SUFFIX;
    }

    public static String makePopularPostAwardName(Post popularPost, int ranking,
        LocalDate startDate,
        LocalDate endDate,
        RankingCriteria rankingCriteria) {
        // 2020년 8월 서울특별시 이색경험 부문 1위
        if (rankingCriteria.equals(RankingCriteria.LAST_MONTH)) {
            return makePopularPostAwardNameByMonth(popularPost, ranking, startDate, endDate);
        }
        throw new UnsupportedOperationException("월간 수상만 지원합니다.");
    }

    private static String makePopularPostAwardNameByMonth(Post popularPost, int ranking,
        LocalDate startDate,
        LocalDate endDate) {
        String year = findYear(startDate, endDate);
        String month = findMonth(startDate, endDate);

        return year
            + "년 "
            + month
            + "월 "
            + popularPost.getArea().getLastDepthName()
            + " "
            + popularPost.getSector().getName()
            + " 부문 "
            + ranking
            + "위";
    }

    private static String findYear(LocalDate startDate, LocalDate endDate) {
        int startDateYear = startDate.getYear();

        if (startDateYear == endDate.getYear()) {
            return Integer.toString(startDateYear);
        }
        throw new IllegalArgumentException("start date 와 end date 의 연도가 같지 않습니다.");
    }

    private static String findMonth(LocalDate startDate, LocalDate endDate) {
        Month startDateMonth = startDate.getMonth();

        if (startDateMonth.equals(endDate.getMonth())) {
            return Integer.toString(startDateMonth.getValue());
        }
        throw new IllegalArgumentException("start date 와 end date 의 연도가 같지 않습니다.");
    }
}
