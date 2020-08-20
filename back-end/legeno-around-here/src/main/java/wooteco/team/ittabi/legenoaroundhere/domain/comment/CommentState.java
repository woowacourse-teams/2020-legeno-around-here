package wooteco.team.ittabi.legenoaroundhere.domain.comment;

import java.util.function.Function;

public enum CommentState {
    PUBLISHED(true, name -> name),
    DELETED(false, name -> null);

    private final boolean available;
    private final Function<String, String> writingChanger;

    CommentState(boolean available, Function<String, String> writingChanger) {
        this.available = available;
        this.writingChanger = writingChanger;
    }

    public boolean isAvailable() {
        return available;
    }

    public String changeWriting(String name) {
        return writingChanger.apply(name);
    }
}
