package wooteco.team.ittabi.legenoaroundhere.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.post.State;
import wooteco.team.ittabi.legenoaroundhere.domain.post.like.Like;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.LikeResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.PostRepository;

@Service
@AllArgsConstructor
public class LikeService {

    private final PostRepository postRepository;
    private final IAuthenticationFacade authenticationFacade;

    @Transactional
    public LikeResponse pressLike(Long postId) {
        User user = (User) authenticationFacade.getPrincipal();
        Post post = findNotDeletedPost(postId);
        Like like = executeLike(post, user);
        return LikeResponse.of(post.getLikeCount(), like.getLikeState());
    }

    private Like executeLike(Post post, User user) {
        if (post.existLikeBy(user)) {
            return inactivateLike(post, user);
        }
        return activateLike(post, user);
    }

    private Like inactivateLike(Post post, User user) {
        Like like = post.findLikeBySameUser(user);
        like.inactivate(post);
        return like;
    }

    private Like activateLike(Post post, User user) {
        Like like = new Like(post, user);
        like.activate(post);
        return like;
    }

    private Post findNotDeletedPost(Long postId) {
        return postRepository.findByIdAndStateNot(postId, State.DELETED)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 POST가 없습니다."));
    }
}
