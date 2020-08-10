package wooteco.team.ittabi.legenoaroundhere.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;

public interface AreaRepository extends JpaRepository<Area, Long> {

    Page<Area> findAllByFullNameIsLike(Pageable pageable, String keywords);
}
