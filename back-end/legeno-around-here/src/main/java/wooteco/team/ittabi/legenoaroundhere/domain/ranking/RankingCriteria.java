package wooteco.team.ittabi.legenoaroundhere.domain.ranking;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.function.Function;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;

public enum RankingCriteria {
    YESTERDAY("yesterday",
        // 어제 0시 0분 0초
        dateTime -> LocalDateTime.of(
            dateTime.minusDays(1).getYear(),
            dateTime.minusDays(1).getMonth(),
            dateTime.minusDays(1).getDayOfMonth(),
            0,
            0
        ),
        // 오늘 0시 0분 0초
        dateTime -> LocalDateTime.of(
            dateTime.getYear(),
            dateTime.getMonth(),
            dateTime.getDayOfMonth(),
            0,
            0
        )
    ),
    LAST_WEEK("week",
        // 일요일 ~ 토요일을 한 주로 하여, 지난주 첫날(일요일)의 이 시각
        dateTime -> dateTime.minusDays(7 + (dateTime.getDayOfWeek().getValue() % 7)),
        // 일요일 ~ 토요일을 한 주로 하여, 이번주 첫날(일요일)의 이 시각
        dateTime -> dateTime.minusDays(dateTime.getDayOfWeek().getValue() % 7)
    ),
    LAST_MONTH("month",
        // 지난달의 시작 (지난달 1일 0시 0분 0초)
        dateTime -> LocalDateTime.of(
            dateTime.minusMonths(1).getYear(),
            dateTime.minusMonths(1).getMonth(),
            1,
            0,
            0
        ),
        // 지난달의 마지막 = 이번달의 시작 (이번달 1일 0시 0분 0초)
        dateTime -> LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), 1, 0, 0)
    ),
    TOTAL("total",
        dateTime -> LocalDateTime.of(1000, 1, 1, 0, 0, 0),
        dateTime -> LocalDateTime.of(9999, 12, 31, 23, 59, 59)
    );

    private final String rankingCriteria;
    private final Function<LocalDateTime, LocalDateTime> calculateStartDate;
    private final Function<LocalDateTime, LocalDateTime> calculateEndDate;

    RankingCriteria(String rankingCriteria,
        Function<LocalDateTime, LocalDateTime> calculateStartDate,
        Function<LocalDateTime, LocalDateTime> calculateEndDate) {
        this.rankingCriteria = rankingCriteria;
        this.calculateStartDate = calculateStartDate;
        this.calculateEndDate = calculateEndDate;
    }

    public static RankingCriteria of(String criteria) {
        return Arrays.stream(values())
            .filter(rc -> rc.isSameRankingCriteria(criteria))
            .findFirst()
            .orElseThrow(() -> new NotExistsException("존재하지 않는 랭킹 기준입니다."));
    }

    private boolean isSameRankingCriteria(String criteria) {
        return this.rankingCriteria.equals(criteria);
    }

    public String getCriteriaName() {
        return this.rankingCriteria;
    }

    public LocalDateTime getStartDate() {
        return this.getStartDate(LocalDateTime.now());
    }

    public LocalDateTime getEndDate() {
        return this.getEndDate(LocalDateTime.now());
    }

    public LocalDateTime getStartDate(LocalDateTime now) {
        return this.calculateStartDate.apply(now);
    }

    public LocalDateTime getEndDate(LocalDateTime now) {
        return this.calculateEndDate.apply(now);
    }
}
