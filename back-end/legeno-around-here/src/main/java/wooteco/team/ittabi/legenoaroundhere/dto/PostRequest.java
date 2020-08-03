package wooteco.team.ittabi.legenoaroundhere.dto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.domain.Post;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PostRequest {

    private String writing;
    private List<MultipartFile> images;

    public PostRequest(String writing) {
        this.writing = writing;
        this.images = Collections.emptyList();
    }

    public PostRequest(String writing, List<MultipartFile> images) {
        this.writing = writing;
        this.images = images;
    }

    public boolean isImagesNull() {
        return Objects.isNull(images);
    }

    public Post toPost() {
        return new Post(writing);
    }

}
