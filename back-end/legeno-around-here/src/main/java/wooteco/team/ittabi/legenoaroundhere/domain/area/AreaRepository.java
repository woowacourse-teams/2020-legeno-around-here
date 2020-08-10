package wooteco.team.ittabi.legenoaroundhere.domain.area;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaRepository extends JpaRepository<Area, Long> {

    Page<Area> findAllByFullNameIsLike(Pageable pageable, String keywords);
}
