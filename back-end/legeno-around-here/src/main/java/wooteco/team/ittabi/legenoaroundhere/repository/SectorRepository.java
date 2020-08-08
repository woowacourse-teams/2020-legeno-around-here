package wooteco.team.ittabi.legenoaroundhere.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Name;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

public interface SectorRepository extends JpaRepository<Sector, Long> {

    Optional<Sector> findByName(Name name);

    List<Sector> findAllByCreator(User user);
}
