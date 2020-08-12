package wooteco.team.ittabi.legenoaroundhere.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.post.State;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByStateNot(Pageable pageable, State state);

    Page<Post> findAllByStateNotAndAreaIn(Pageable pageable, State state, List<Area> areas);

    Page<Post> findAllByStateNotAndSectorIn(Pageable pageable, State state, List<Sector> sectors);

    Page<Post> findAllByStateNotAndAreaInAndSectorIn(Pageable pageable, State state,
        List<Area> areas, List<Sector> sectors);

    Optional<Post> findByIdAndStateNot(Long id, State state);
}
