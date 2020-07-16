package wooteco.team.ittabi.legenoaroundhere.dto;

import java.util.Objects;

public class PostCreateRequest {

    private String writing;

    public PostCreateRequest() {
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
        PostCreateRequest that = (PostCreateRequest) o;
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
