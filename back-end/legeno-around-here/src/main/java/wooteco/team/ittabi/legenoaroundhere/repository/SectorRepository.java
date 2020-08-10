package wooteco.team.ittabi.legenoaroundhere.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.SectorState;

public interface SectorRepository extends JpaRepository<Sector, Long> {

    Page<Sector> findAllByStateIn(Pageable pageable, Iterable<SectorState> states);
}
