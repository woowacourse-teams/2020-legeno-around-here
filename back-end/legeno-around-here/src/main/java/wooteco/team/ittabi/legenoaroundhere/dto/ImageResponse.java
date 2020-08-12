package wooteco.team.ittabi.legenoaroundhere.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.post.image.Image;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ImageResponse {

    private Long id;
    private String name;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static ImageResponse of(Image image) {
        return new ImageResponse(image.getId(), image.getName(), image.getUrl(),
            image.getCreatedAt(), image.getModifiedAt());
    }

    public static List<ImageResponse> listOf(List<Image> images) {
        return images.stream()
            .map(ImageResponse::of)
            .collect(Collectors.toList());
    }
}
