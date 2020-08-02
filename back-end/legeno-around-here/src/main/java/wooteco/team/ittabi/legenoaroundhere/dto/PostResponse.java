package wooteco.team.ittabi.legenoaroundhere.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.Post;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class PostResponse {

    private Long id;
    private String writing;
    private List<ImageResponse> images;

    public static PostResponse of(Post post) {
        List<ImageResponse> imageResponses = ImageResponse.listOf(post.getImages());
        return new PostResponse(post.getId(), post.getWriting(), imageResponses);
    }

}
