package wooteco.team.ittabi.legenoaroundhere.domain;

public enum State {
    PUBLISHED(true),
    DELETED(false);

    private final boolean available;

    State(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }
}
