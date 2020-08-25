package wooteco.team.ittabi.legenoaroundhere.dto;

import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.post.PostReport;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

public class PostReportAssembler {

    public static PostReport assemble(PostReportCreateRequest postReportCreateRequest,
        Post post, User user) {
        return PostReport.builder()
            .writing(postReportCreateRequest.getWriting())
            .post(post)
            .creator(user)
            .build();
    }
}
