package wooteco.team.ittabi.legenoaroundhere.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.Post;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class PostResponse {

    private Long id;
    private String writing;
    private UserResponse user;

    public static PostResponse of(Post post) {
        return new PostResponse(post.getId(), post.getWriting(), UserResponse.from(post.getUser()));
    }

    public static List<PostResponse> listOf(List<Post> posts) {
        return posts.stream()
            .map(PostResponse::of)
            .collect(Collectors.toList());
    }
}
