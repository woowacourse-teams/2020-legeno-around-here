package wooteco.team.ittabi.legenoaroundhere.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.area.LegalArea;

public interface LegalAreaRepository extends JpaRepository<LegalArea, Long> {
    LegalArea findByCode(String code);
}
