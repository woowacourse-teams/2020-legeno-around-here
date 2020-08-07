package wooteco.team.ittabi.legenoaroundhere.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.State;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByStateNot(State state);

    Optional<Post> findByIdAndStateNot(Long id, State state);
}
