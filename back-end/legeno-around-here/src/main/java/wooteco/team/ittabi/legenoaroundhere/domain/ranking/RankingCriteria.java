package wooteco.team.ittabi.legenoaroundhere.domain.ranking;

import java.time.LocalDateTime;
import java.util.Arrays;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;

public enum RankingCriteria {
    YESTERDAY("yesterday",
        LocalDateTime.of(
            LocalDateTime.now().minusDays(1).getYear(),
            LocalDateTime.now().minusDays(1).getMonth(),
            LocalDateTime.now().minusDays(1).getDayOfMonth(),
            0,
            0),
        LocalDateTime.of(
            LocalDateTime.now().getYear(),
            LocalDateTime.now().getMonth(),
            LocalDateTime.now().getDayOfMonth(),
            0,
            0)),
    LAST_WEEK("week",
        LocalDateTime.now().minusDays(7 + (LocalDateTime.now().getDayOfWeek().getValue() % 7)),
        LocalDateTime.now().minusDays(LocalDateTime.now().getDayOfWeek().getValue() % 7)),
    LAST_MONTH("month",
        LocalDateTime.of(
            LocalDateTime.now().minusMonths(1).getYear(),
            LocalDateTime.now().minusMonths(1).getMonth(),
            1,
            0,
            0),
        LocalDateTime.of(
            LocalDateTime.now().getYear(),
            LocalDateTime.now().getMonth(),
            1,
            0,
            0)),
    TOTAL("total", LocalDateTime.of(1000, 1, 1, 0, 0, 0),
        LocalDateTime.of(9999, 12, 31, 23, 59, 59));

    private final String rankingCriteria;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    RankingCriteria(String rankingCriteria, LocalDateTime startDate,
        LocalDateTime endDate) {
        this.rankingCriteria = rankingCriteria;
        this.startDate = startDate;
        this.endDate = endDate;
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
        return this.startDate;
    }

    public LocalDateTime getEndDate() {
        return this.endDate;
    }
}
