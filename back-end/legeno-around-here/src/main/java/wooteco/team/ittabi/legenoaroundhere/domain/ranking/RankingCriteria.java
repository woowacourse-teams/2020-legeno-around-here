package wooteco.team.ittabi.legenoaroundhere.domain.ranking;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.function.Function;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;

public enum RankingCriteria {
    YESTERDAY("yesterday",
        dateTime -> LocalDateTime.of(
            dateTime.minusDays(1).getYear(),
            dateTime.minusDays(1).getMonth(),
            dateTime.minusDays(1).getDayOfMonth(),
            0,
            0
        ),
        dateTime -> LocalDateTime.of(
            dateTime.getYear(),
            dateTime.getMonth(),
            dateTime.getDayOfMonth(),
            0,
            0
        )
    ),
    LAST_WEEK("week",
        dateTime -> dateTime.minusDays(7 + (dateTime.getDayOfWeek().getValue() % 7)),
        dateTime -> dateTime.minusDays(dateTime.getDayOfWeek().getValue() % 7)
    ),
    LAST_MONTH("month",
        dateTime -> LocalDateTime.of(
            dateTime.minusMonths(1).getYear(),
            dateTime.minusMonths(1).getMonth(),
            1,
            0,
            0
        ),
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
