package wooteco.team.ittabi.legenoaroundhere.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.award.SectorCreatorAward;

public interface SectorCreatorAwardRepository extends JpaRepository<SectorCreatorAward, Long> {

    List<SectorCreatorAward> findAllByAwardee_Id(Long awardeeId);
}
