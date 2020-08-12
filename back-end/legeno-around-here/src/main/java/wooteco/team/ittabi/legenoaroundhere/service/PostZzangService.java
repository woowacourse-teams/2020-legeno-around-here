package wooteco.team.ittabi.legenoaroundhere.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.post.State;
import wooteco.team.ittabi.legenoaroundhere.domain.post.zzang.PostZzang;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.PostZzangResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.PostRepository;

@Service
@AllArgsConstructor
public class PostZzangService {

    private final PostRepository postRepository;
    private final IAuthenticationFacade authenticationFacade;

    @Transactional
    public PostZzangResponse pressPostZzang(Long postId) {
        User user = (User) authenticationFacade.getPrincipal();
        Post post = findNotDeletedPost(postId);
        PostZzang postZzang = executePostZzang(post, user);
        return PostZzangResponse.of(post.getPostZzangCount(), postZzang.getZzangState());
    }

    private PostZzang executePostZzang(Post post, User user) {
        if (post.existPostZzangBy(user)) {
            return inactivatePostZzang(post, user);
        }
        return activatePostZzang(post, user);
    }

    private PostZzang inactivatePostZzang(Post post, User user) {
        PostZzang postZzang = post.findPostZzangBy(user);
        postZzang.inactivate(post);
        return postZzang;
    }

    private PostZzang activatePostZzang(Post post, User user) {
        PostZzang postZzang = new PostZzang(post, user);
        postZzang.activate(post);
        return postZzang;
    }

    private Post findNotDeletedPost(Long postId) {
        return postRepository.findByIdAndStateNot(postId, State.DELETED)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 POST가 없습니다."));
    }
}
