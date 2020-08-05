package wooteco.team.ittabi.legenoaroundhere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;

@NoRepositoryBean
public interface AreaRepository<T extends Area> extends JpaRepository<T, Long> {
    Area findByCode(String code);
}
