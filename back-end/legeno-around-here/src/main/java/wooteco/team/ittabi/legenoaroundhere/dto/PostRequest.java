package wooteco.team.ittabi.legenoaroundhere.dto;

import java.util.Objects;

public class PostRequest {

    private String writing;

    public PostRequest() {
    }

    public PostRequest(String writing) {
        this.writing = writing;
    }

    public String getWriting() {
        return writing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PostRequest that = (PostRequest) o;
        return Objects.equals(writing, that.writing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(writing);
    }

    @Override
    public String toString() {
        return "PostCreateRequest{" +
            "writing='" + writing + '\'' +
            '}';
    }
}
