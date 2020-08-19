package wooteco.team.ittabi.legenoaroundhere.dto;

import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

public class PostAssembler {

    public static Post assemble(PostCreateRequest postCreateRequest, Area area, Sector sector,
        User user) {
        return Post.builder()
            .writing(postCreateRequest.getWriting())
            .area(area)
            .sector(sector)
            .creator(user)
            .build();
    }
}
