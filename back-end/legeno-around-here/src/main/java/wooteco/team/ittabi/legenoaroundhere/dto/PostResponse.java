package wooteco.team.ittabi.legenoaroundhere.dto;

import java.util.Objects;

public class PostResponse {

    private Long id;
    private String writing;

    public PostResponse() {
    }

    public PostResponse(Long id, String writing) {
        this.id = id;
        this.writing = writing;
    }

    public Long getId() {
        return id;
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
        PostResponse that = (PostResponse) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(writing, that.writing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, writing);
    }

    @Override
    public String toString() {
        return "PostResponse{" +
            "id=" + id +
            ", writing='" + writing + '\'' +
            '}';
    }
}
