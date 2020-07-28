package wooteco.team.ittabi.legenoaroundhere.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllByPostId(Long id);
}
