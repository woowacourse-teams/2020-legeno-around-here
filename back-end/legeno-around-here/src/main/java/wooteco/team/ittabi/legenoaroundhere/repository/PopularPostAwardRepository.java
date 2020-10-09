package wooteco.team.ittabi.legenoaroundhere.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.award.PopularPostAward;

public interface PopularPostAwardRepository extends
    JpaRepository<PopularPostAward, Long> {

    List<PopularPostAward> findAllByAwardee_Id(Long awardeeId);
}
