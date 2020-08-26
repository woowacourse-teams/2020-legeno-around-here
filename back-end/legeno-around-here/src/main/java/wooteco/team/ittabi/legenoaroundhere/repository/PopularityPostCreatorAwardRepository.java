package wooteco.team.ittabi.legenoaroundhere.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.award.PopularityPostCreatorAward;

public interface PopularityPostCreatorAwardRepository extends
    JpaRepository<PopularityPostCreatorAward, Long> {

    List<PopularityPostCreatorAward> findAllByAwardee_Id(Long awardeeId);
}
