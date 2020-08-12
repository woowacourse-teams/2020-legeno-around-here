package wooteco.team.ittabi.legenoaroundhere.dto;

import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PostCreateRequest {

    private String writing;
    private List<MultipartFile> images;
    private Long areaId;
    private Long sectorId;

    public boolean isImagesNull() {
        return Objects.isNull(images);
    }

    public Post toPost(Area area, Sector sector, User user) {
        return Post.builder()
            .writing(writing)
            .area(area)
            .sector(sector)
            .creator(user)
            .build();
    }
}
