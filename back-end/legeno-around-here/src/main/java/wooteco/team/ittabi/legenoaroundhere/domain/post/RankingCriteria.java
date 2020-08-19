package wooteco.team.ittabi.legenoaroundhere.domain.post;

public enum RankingCriteria {
    LAST_DAY("day"),
    LAST_WEEK("week"),
    LAST_MONTH("month"),
    TOTAL("total");

    private final String criteria;

    RankingCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getCriteriaName() {
        return this.criteria;
    }
}
