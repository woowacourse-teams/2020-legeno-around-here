package wooteco.team.ittabi.legenoaroundhere.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.post.like.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostIdAndCreatorId(Long postId, Long creatorId);

    boolean existsByPostIdAndCreatorId(Long postId, Long creatorId);
}
