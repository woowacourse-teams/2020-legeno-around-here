package wooteco.team.ittabi.legenoaroundhere.domain.post;

import java.time.LocalDateTime;
import java.util.Arrays;

public enum RankingCriteria {
    LAST_DAY("day", LocalDateTime.MIN, LocalDateTime.MAX),
    LAST_WEEK("week", LocalDateTime.MIN, LocalDateTime.MAX),
    LAST_MONTH("month", LocalDateTime.MIN, LocalDateTime.MAX),
    TOTAL("total", LocalDateTime.MIN, LocalDateTime.MAX);

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
            .filter(rc -> rc.rankingCriteria.equals(criteria))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 랭킹 기준입니다."));
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
