package wooteco.team.ittabi.legenoaroundhere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
