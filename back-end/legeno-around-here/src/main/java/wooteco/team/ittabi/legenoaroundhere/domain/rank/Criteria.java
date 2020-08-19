package wooteco.team.ittabi.legenoaroundhere.domain.rank;

public enum Criteria {
    LAST_DAY("day"),
    LAST_WEEK("week"),
    LAST_MONTH("month"),
    TOTAL("total");

    private final String criteria;

    Criteria(String criteria) {
        this.criteria = criteria;
    }

    public String getCriteriaName() {
        return this.criteria;
    }
}
