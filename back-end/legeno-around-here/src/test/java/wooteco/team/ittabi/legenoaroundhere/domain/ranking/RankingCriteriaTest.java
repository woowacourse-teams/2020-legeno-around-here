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
        LocalDateTime now = LocalDateTime.of(2020, 3, 11, 14, 20, 0);

        LocalDateTime lastMonthStart = LocalDateTime.of(2020, 2, 1, 0, 0, 0);

        // 일요일 ~ 토요일을 한 주로 하여, 지난주 첫날(일요일)의 이 시각
        LocalDateTime lastWeekStart = LocalDateTime.of(2020, 3, 1, 14, 20, 0);

        return Stream.of(
            Arguments.of(RankingCriteria.LAST_MONTH,
                LocalDateTime.of(2020, 3, 11, 14, 20, 0),    // now
                LocalDateTime.of(2020, 2, 1, 0, 0, 0)
            ),
            Arguments.of(RankingCriteria.LAST_MONTH,
                LocalDateTime.of(2020, 10, 12, 4, 0, 0),    // now
                LocalDateTime.of(2020, 9, 1, 0, 0, 0)
            ),
            Arguments.of(RankingCriteria.LAST_WEEK,
                LocalDateTime.of(2020, 3, 11, 14, 20, 0),   // now
                LocalDateTime.of(2020, 3, 1, 14, 20, 0)    // 지난주 일요일 이시각
            ),
            Arguments.of(RankingCriteria.LAST_WEEK,
                LocalDateTime.of(2020, 10, 12, 4, 0, 0),   // now
                LocalDateTime.of(2020, 10, 4, 4, 0, 0)    // 지난주 일요일 이시각
            ),
            Arguments.of(RankingCriteria.YESTERDAY,
                LocalDateTime.of(2020, 3, 11, 14, 20, 0),   // now
                LocalDateTime.of(2020, 3, 10, 0, 0, 0)    // 지난주 일요일 이시각
            ),
            Arguments.of(RankingCriteria.YESTERDAY,
                LocalDateTime.of(2020, 10, 12, 4, 0, 0),   // now
                LocalDateTime.of(2020, 10, 11, 0, 0, 0)
            )
        );
    }

    private static Stream<Arguments> getEndDateTestcase() {
        return Stream.of(
            Arguments.of(RankingCriteria.LAST_MONTH,
                LocalDateTime.of(2020, 3, 11, 14, 20, 0),    // now
                LocalDateTime.of(2020, 3, 1, 0, 0, 0)
            ),
            Arguments.of(RankingCriteria.LAST_WEEK,
                LocalDateTime.of(2020, 3, 11, 14, 20, 0),
                LocalDateTime.of(2020, 3, 8, 14, 20, 0)
            ),
            Arguments.of(RankingCriteria.YESTERDAY,
                LocalDateTime.of(2020, 3, 11, 14, 20, 0),
                LocalDateTime.of(2020, 3, 11, 0, 0, 0)
            )
        );
    }
}
