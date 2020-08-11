package wooteco.team.ittabi.legenoaroundhere.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.post.State;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostIdAndStateNot(Long post_id, State state);

    Optional<Comment> findByIdAndStateNot(Long id, State state);
}
