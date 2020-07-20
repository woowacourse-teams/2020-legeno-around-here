package wooteco.team.ittabi.legenoaroundhere.domain;

import javax.persistence.Entity;

@Entity
public class Post extends BaseEntity {

    private static final int LIMIT_LENGTH = 20;

    private String writing;

    protected Post() {
    }

    public Post(String writing) {
        validateLength(writing);
        this.writing = writing;
    }

    private void validateLength(String writing) {
        if (writing.length() > LIMIT_LENGTH) {
            throw new IllegalArgumentException(LIMIT_LENGTH + "글자를 초과했습니다!");
        }
    }

    public String getWriting() {
        return writing;
    }

    public void setWriting(String writing) {
        this.writing = writing;
    }

    @Override
    public String toString() {
        return "Post{" +
            "writing='" + writing + '\'' +
            '}';
    }
}
