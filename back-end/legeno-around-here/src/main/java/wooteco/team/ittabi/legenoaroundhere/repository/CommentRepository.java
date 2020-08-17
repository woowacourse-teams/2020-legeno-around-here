package wooteco.team.ittabi.legenoaroundhere.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.comment.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostId(Long postId);
}
