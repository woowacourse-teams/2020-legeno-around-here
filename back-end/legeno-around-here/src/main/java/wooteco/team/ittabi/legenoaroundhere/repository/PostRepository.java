package wooteco.team.ittabi.legenoaroundhere.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.post.State;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByStateNot(Pageable pageable, State state);

    Optional<Post> findByIdAndStateNot(Long id, State state);
}
