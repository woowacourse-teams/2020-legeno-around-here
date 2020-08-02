package wooteco.team.ittabi.legenoaroundhere.dto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import wooteco.team.ittabi.legenoaroundhere.domain.Image;
import wooteco.team.ittabi.legenoaroundhere.domain.Post;

public class PostResponse {

    private Long id;
    private String writing;

    private List<Image> images;

    public PostResponse() {
    }

    public PostResponse(Long id, String writing) {
        this.id = id;
        this.writing = writing;
        this.images = Collections.emptyList();
    }

    public PostResponse(Long id, String writing, List<Image> images) {
        this.id = id;
        this.writing = writing;
        this.images = images;
    }

    public static PostResponse of(Post post, List<Image> images) {
        return new PostResponse(post.getId(), post.getWriting(), images);
    }

    public Long getId() {
        return id;
    }

    public String getWriting() {
        return writing;
    }

    public List<Image> getImages() {
        return images;
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
