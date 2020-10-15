package wooteco.team.ittabi.legenoaroundhere.service.award;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.AwardResponse;

@Slf4j
@Service
@AllArgsConstructor
public class AwardService {

    private final IAuthenticationFacade authenticationFacade;
    private final PopularPostAwardService popularPostAwardService;
    private final SectorAwardService sectorAwardService;

    public List<AwardResponse> findAwards(Long awardeeId) {
        List<AwardResponse> awardResponses = new ArrayList<>();
        awardResponses.addAll(popularPostAwardService.findPopularPostAwards(awardeeId));
        awardResponses.addAll(sectorAwardService.findSectorCreatorAwards(awardeeId));

        return awardResponses;
    }

    public List<AwardResponse> findMyAwards() {
        User user = (User) authenticationFacade.getPrincipal();

        return findAwards(user.getId());
    }

    public void createPopularPostAwards(LocalDateTime awardingTime) {
        popularPostAwardService.createPopularPostAwards(awardingTime);
    }

    public void giveSectorCreatorAward(Sector sector) {
        sectorAwardService.giveSectorCreatorAward(sector);
    }
}
