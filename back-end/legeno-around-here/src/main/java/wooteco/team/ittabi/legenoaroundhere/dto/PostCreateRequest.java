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
import wooteco.team.ittabi.legenoaroundhere.domain.Post;
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
    private Long sectorId;

    public boolean isImagesNull() {
        return Objects.isNull(images);
    }

    public Post toPost(User user, Sector sector) {
        return new Post(user, writing, sector);
    }
}
