package wooteco.team.ittabi.legenoaroundhere.domain.util;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.ranking.RankingCriteria;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AwardNameMaker {

    private static final String SECTOR_CREATOR_AWARD_NAME_SUFFIX = " 부문 창시자";

    public static String makeSectorCreatorAwardName(Sector sector) {
        return sector.getName() + SECTOR_CREATOR_AWARD_NAME_SUFFIX;
    }

    public static String makePopularPostAwardName(Post popularPost, int ranking, Area targetArea,
        LocalDate startDate,
        LocalDate endDate,
        RankingCriteria rankingCriteria) {

        if (rankingCriteria.equals(RankingCriteria.LAST_MONTH)) {
            return makePopularPostAwardNameByMonth(
                popularPost, ranking, targetArea, startDate, endDate);
        }
        throw new UnsupportedOperationException("월간 수상만 지원합니다.\n"
            + "현재 " + rankingCriteria.getCriteriaName() + "으로"
            + "상 이름을 만들어주길 요청하셨습니다.");
    }

    private static String makePopularPostAwardNameByMonth(Post popularPost, int ranking,
        Area targetArea, LocalDate startDate, LocalDate endDate) {
        validateDateForMonthlyPostAward(startDate, endDate);

        String year = Integer.toString(startDate.getYear());
        String month = Integer.toString(startDate.getMonthValue());

        return year
            + "년 "
            + month
            + "월 "
            + targetArea.getLastDepthName()
            + " "
            + popularPost.getSector().getName()
            + " 부문 "
            + ranking
            + "위";
    }

    private static void validateDateForMonthlyPostAward(LocalDate startDate, LocalDate endDate) {
        validateYearForMonthlyPostAward(startDate, endDate);
        validateMonthForMonthlyPostAward(startDate, endDate);
        validateDayForMonthlyPostAward(startDate, endDate);
    }

    private static void validateYearForMonthlyPostAward(LocalDate startDate, LocalDate endDate) {
        if (startDate.getYear() == endDate.getYear()) {
            return;
        }
        if ((startDate.getYear() + 1 == endDate.getYear()) && startDate.getMonthValue() == 12) {
            // startDate : (n-1)년 12월 1일 && endDate : n년 1월 1일
            return;
        }
        throw new IllegalArgumentException(
            "start date 와 end date 의 연도가 잘못되었습니다.\n"
                + "start date : "
                + startDate.getMonthValue() + " 월 "
                + startDate.getDayOfMonth() + "일\n"
                + "end date : "
                + endDate.getMonthValue() + " 월 "
                + endDate.getDayOfMonth() + "일"
        );
    }

    private static void validateMonthForMonthlyPostAward(LocalDate startDate, LocalDate endDate) {
        if (startDate.getMonthValue() + 1 != endDate.getMonthValue()) {
            throw new IllegalArgumentException(
                "start date 의 달이 end date 의 달보다 1 작아야합니다.\n"
                    + "start date : "
                    + startDate.getMonthValue() + " 월 "
                    + startDate.getDayOfMonth() + "일\n"
                    + "end date : "
                    + endDate.getMonthValue() + " 월 "
                    + endDate.getDayOfMonth() + "일"
            );
        }
    }

    private static void validateDayForMonthlyPostAward(LocalDate startDate, LocalDate endDate) {
        if (startDate.getDayOfMonth() != 1 || endDate.getDayOfMonth() != 1) {
            throw new IllegalArgumentException(
                "start date 와 end date 는 1일이어야 합니다.\n"
                    + "start date : "
                    + startDate.getMonthValue() + " 월 "
                    + startDate.getDayOfMonth() + "일\n"
                    + "end date : "
                    + endDate.getMonthValue() + " 월 "
                    + endDate.getDayOfMonth() + "일"
            );
        }
    }
}
