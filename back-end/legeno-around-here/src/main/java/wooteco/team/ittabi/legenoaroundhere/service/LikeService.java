package wooteco.team.ittabi.legenoaroundhere.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.post.State;
import wooteco.team.ittabi.legenoaroundhere.domain.post.like.Like;
import wooteco.team.ittabi.legenoaroundhere.domain.post.like.LikeState;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.LikeResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.LikeRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.PostRepository;

@Service
@AllArgsConstructor
public class LikeService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public LikeResponse pressLike(Long postId, User user) {
        Post post = findPost(postId);
        boolean isLikeAlreadyExists = isLikeExists(post, user);

        if (isLikeAlreadyExists) {
            return inactivateLike(post, user);
        }
        return activateLike(post, user);
    }

    private boolean isLikeExists(Post post, User user) {
        return likeRepository.existsByPostIdAndUserId(post.getId(), user.getId());
    }

    private LikeResponse inactivateLike(Post post, User user) {
        likeRepository.deleteByPostIdAndUserId(post.getId(), user.getId());
        post.minusLikeCount();
        return LikeResponse.of(post.getLikeCount().getLikeCount(), LikeState.INACTIVATED);
    }

    private LikeResponse activateLike(Post post, User user) {
        likeRepository.save(new Like(post, user));
        post.plusLikeCount();
        return LikeResponse.of(post.getLikeCount().getLikeCount(), LikeState.ACTIVATED);
    }

    @Transactional(readOnly = true)
    public LikeResponse findByPostId(Long postId, User user) {
        Post post = findPost(postId);
        if (isLikeExists(post, user)) {
            return LikeResponse.of(post.getLikeCount().getLikeCount(), LikeState.ACTIVATED);
        }
        return LikeResponse.of(post.getLikeCount().getLikeCount(), LikeState.INACTIVATED);
    }

    private Post findPost(Long postId) {
        return postRepository.findByIdAndStateNot(postId, State.DELETED)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 POST가 없습니다."));
    }
}
