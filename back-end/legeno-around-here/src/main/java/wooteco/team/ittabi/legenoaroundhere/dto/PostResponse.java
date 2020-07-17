package wooteco.team.ittabi.legenoaroundhere.dto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import wooteco.team.ittabi.legenoaroundhere.domain.Post;

public class PostResponse {

    private Long id;
    private String writing;

    public PostResponse() {
    }

    public PostResponse(Long id, String writing) {
        this.id = id;
        this.writing = writing;
    }

    public static PostResponse of(Post post) {
        return new PostResponse(post.getId(), post.getWriting());
    }

    public static List<PostResponse> listOf(List<Post> posts) {
        return posts.stream()
            .map(PostResponse::of)
            .collect(Collectors.toList());
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
