package wooteco.team.ittabi.legenoaroundhere.domain;

public class Post {

    private static final int LIMIT_LENGTH = 20;

    private Long id;
    private String writing;

    public Post(String writing) {
        validateLength(writing);
        this.writing = writing;
    }

    public Post(Long id, String writing) {
        this.id = id;
        this.writing = writing;
    }

    private void validateLength(String writing) {
        if (writing.length() > LIMIT_LENGTH) {
            throw new IllegalArgumentException(LIMIT_LENGTH + "글자를 초과했습니다!");
        }
    }

    public Long getId() {
        return id;
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
            "id=" + id +
            ", writing='" + writing + '\'' +
            '}';
    }
}
