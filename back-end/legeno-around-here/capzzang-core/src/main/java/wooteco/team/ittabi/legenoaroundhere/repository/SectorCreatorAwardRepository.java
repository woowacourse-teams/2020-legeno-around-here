package wooteco.team.ittabi.legenoaroundhere.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.award.SectorCreatorAward;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;

public interface SectorCreatorAwardRepository extends JpaRepository<SectorCreatorAward, Long> {

    List<SectorCreatorAward> findAllByAwardee_Id(Long awardeeId);

    Optional<SectorCreatorAward> findBySector(Sector sector);
}
