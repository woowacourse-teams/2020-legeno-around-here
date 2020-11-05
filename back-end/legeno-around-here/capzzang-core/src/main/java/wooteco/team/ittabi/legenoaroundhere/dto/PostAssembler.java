package wooteco.team.ittabi.legenoaroundhere.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.post.image.PostImage;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostAssembler {

    public static Post assemble(PostCreateRequest postCreateRequest, List<PostImage> postImages,
        Area area, Sector sector, User user) {
        return Post.builder()
            .writing(postCreateRequest.getWriting())
            .postImages(postImages)
            .area(area)
            .sector(sector)
            .creator(user)
            .build();
    }
}
