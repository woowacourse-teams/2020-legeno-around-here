package wooteco.team.ittabi.legenoaroundhere.dto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.domain.Post;

public class PostRequest {

    private String writing;
    private List<MultipartFile> images;

    public PostRequest() {
    }

    public PostRequest(String writing) {
        this.writing = writing;
        this.images = Collections.emptyList();
    }

    public PostRequest(String writing, List<MultipartFile> images) {
        this.writing = writing;
        this.images = images;
    }

    public Post toPost() {
        return new Post(writing);
    }

    public String getWriting() {
        return writing;
    }

    public List<MultipartFile> getImages() {
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
        PostRequest that = (PostRequest) o;
        return Objects.equals(writing, that.writing) &&
            Objects.equals(images, that.images);
    }

    @Override
    public int hashCode() {
        return Objects.hash(writing, images);
    }

    @Override
    public String toString() {
        return "PostRequest{" +
            "writing='" + writing + '\'' +
            ", images=" + images +
            '}';
    }
}
