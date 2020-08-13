package wooteco.team.ittabi.legenoaroundhere.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllBy(Pageable pageable);

    Page<Post> findAllByAreaIn(Pageable pageable, List<Area> areas);

    Page<Post> findAllBySectorIn(Pageable pageable, List<Sector> sectors);

    Page<Post> findAllByAreaInAndSectorIn(Pageable pageable, List<Area> areas,
        List<Sector> sectors);
}
