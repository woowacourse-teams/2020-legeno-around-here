package wooteco.team.ittabi.legenoaroundhere.domain.ranking;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RankingCriteriaTest {

    @DisplayName("of")
    @Test
    void of() {
        assertThat(RankingCriteria.of("yesterday")).isEqualTo(RankingCriteria.YESTERDAY);
        assertThat(RankingCriteria.of("month")).isEqualTo(RankingCriteria.LAST_MONTH);
        assertThat(RankingCriteria.of("week")).isEqualTo(RankingCriteria.LAST_WEEK);
        assertThat(RankingCriteria.of("total")).isEqualTo(RankingCriteria.TOTAL);
    }

    @DisplayName("getCriteriaName")
    @Test
    void getCriteriaName() {
        assertThat(RankingCriteria.TOTAL.getCriteriaName()).isEqualTo("total");
        assertThat(RankingCriteria.LAST_MONTH.getCriteriaName()).isEqualTo("month");
        assertThat(RankingCriteria.LAST_WEEK.getCriteriaName()).isEqualTo("week");
        assertThat(RankingCriteria.YESTERDAY.getCriteriaName()).isEqualTo("yesterday");
    }

    @DisplayName("RankingCriteria 시작 시각 구하기")
    @ParameterizedTest
    @MethodSource("getStartDateTestcase")
    void getStartDate(RankingCriteria rankingCriteria, LocalDateTime now, LocalDateTime expected) {
        assertThat(rankingCriteria.getStartDate(now)).isEqualTo(expected);
    }

    @DisplayName("RankingCriteria 끝 시각 구하기")
    @ParameterizedTest
    @MethodSource("getEndDateTestcase")
    void getEndDate(RankingCriteria rankingCriteria, LocalDateTime now, LocalDateTime expected) {
        assertThat(rankingCriteria.getEndDate(now)).isEqualTo(expected);
    }

    private static Stream<Arguments> getStartDateTestcase() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastMonthStart = LocalDateTime.of(
            now.minusMonths(1).getYear(),
            now.minusMonths(1).getMonth(),
            1,
            0,
            0
        );
        LocalDateTime lastWeekStart = now.minusDays(7 + (now.getDayOfWeek().getValue() % 7));

        return Stream.of(
            Arguments.of(RankingCriteria.LAST_MONTH, now, lastMonthStart),
            Arguments.of(RankingCriteria.LAST_WEEK, now, lastWeekStart)
        );
    }

    private static Stream<Arguments> getEndDateTestcase() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime lastMonthEnd = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0);
        LocalDateTime lastWeekEnd = now.minusDays(now.getDayOfWeek().getValue() % 7);

        return Stream.of(
            Arguments.of(RankingCriteria.LAST_MONTH, now, lastMonthEnd),
            Arguments.of(RankingCriteria.LAST_WEEK, now, lastWeekEnd)
        );
    }
}