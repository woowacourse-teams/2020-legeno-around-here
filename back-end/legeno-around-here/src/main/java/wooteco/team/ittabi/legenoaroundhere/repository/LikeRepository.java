package wooteco.team.ittabi.legenoaroundhere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.post.like.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByPostIdAndUserId(Long postId, Long userId);

    void deleteByPostIdAndUserId(Long postId, Long userId);
}
