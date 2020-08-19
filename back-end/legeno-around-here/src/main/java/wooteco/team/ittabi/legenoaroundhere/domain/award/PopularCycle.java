package wooteco.team.ittabi.legenoaroundhere.domain.award;

public enum PopularCycle {
    YEARLY("연간"),
    MONTHLY("월간"),
    WEEKLY("주간"),
    DAILY("일간");

    private final String name;

    PopularCycle(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
