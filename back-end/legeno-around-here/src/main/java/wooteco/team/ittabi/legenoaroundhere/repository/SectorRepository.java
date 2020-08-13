package wooteco.team.ittabi.legenoaroundhere.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Name;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.SectorState;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

public interface SectorRepository extends JpaRepository<Sector, Long> {

    List<Sector> findAllByName(Name name);

    Page<Sector> findAllByCreator(Pageable pageable, User user);

    Page<Sector> findAllByStateInAndNameIsLike(Pageable pageable, Iterable<SectorState> states,
        Name keyword);

    List<Sector> findTop20ByStateInAndOrderByPosts(Iterable<SectorState> states);

}
