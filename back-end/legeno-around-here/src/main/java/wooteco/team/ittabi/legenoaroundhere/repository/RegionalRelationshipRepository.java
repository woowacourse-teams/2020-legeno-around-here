package wooteco.team.ittabi.legenoaroundhere.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.area.RegionalRelationship;

public interface RegionalRelationshipRepository extends JpaRepository<RegionalRelationship, Long> {
    List<RegionalRelationship> findByArea(Area area);
    List<RegionalRelationship> findByAreaCode(String code);
}
