package wooteco.team.ittabi.legenoaroundhere.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;

public interface AreaRepository extends JpaRepository<Area, Long> {

    Page<Area> findAllByFullNameIsLike(Pageable pageable, String keyword);

    @Query(value = "SELECT * FROM area WHERE deleted_at IS NULL AND full_name LIKE (SELECT CONCAT(full_name,'%') FROM area WHERE id = :areaId)", nativeQuery = true)
    List<Area> findSubAreasById(@Param("areaId") Long areaId);
}
