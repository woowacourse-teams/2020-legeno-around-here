package wooteco.team.ittabi.legenoaroundhere.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.award.SectorCreatorAward;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.domain.util.AwardNameMaker;
import wooteco.team.ittabi.legenoaroundhere.dto.AwardResponse;
import wooteco.team.ittabi.legenoaroundhere.repository.PopularityPostCreatorAwardRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.SectorCreatorAwardRepository;

@Slf4j
@Service
@AllArgsConstructor
public class AwardService {    // Todo: 테스트 없음 (만들어야댐)

    private final PopularityPostCreatorAwardRepository popularityPostCreatorAwardRepository;
    private final SectorCreatorAwardRepository sectorCreatorAwardRepository;
    private final IAuthenticationFacade authenticationFacade;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public List<AwardResponse> findAwards(Long awardeeId) {
        List<AwardResponse> awardResponses = new ArrayList<>();
        awardResponses.addAll(findPopularityPostCreatorAwards(awardeeId));
        awardResponses.addAll(findSectorCreatorAwards(awardeeId));

        return awardResponses;
    }

    private List<AwardResponse> findPopularityPostCreatorAwards(Long awardeeId) {
        return popularityPostCreatorAwardRepository.findAllByAwardee_Id(awardeeId)
            .stream()
            .map(AwardResponse::of)
            .collect(Collectors.toList());
    }

    private List<AwardResponse> findSectorCreatorAwards(Long awardeeId) {
        return sectorCreatorAwardRepository.findAllByAwardee_Id(awardeeId)
            .stream()
            .map(AwardResponse::of)
            .collect(Collectors.toList());
    }

    public List<AwardResponse> findMyAwards() {
        User user = (User) authenticationFacade.getPrincipal();

        return findAwards(user.getId());
    }

    @Transactional
    void giveSectorCreatorAward(Sector sector) {
        if (sectorCreatorAwardRepository.findBySector(sector).isPresent()) {
            log.info("기존에 수상 이력이 있는 부문입니다.");
            return;
        }
        SectorCreatorAward sectorCreatorAward = SectorCreatorAward.builder()
            .name(AwardNameMaker.makeSectorCreatorAwardName(sector))
            .awardee(sector.getCreator())
            .sector(sector)
            .date(LocalDate.now())
            .build();
        sectorCreatorAwardRepository.save(sectorCreatorAward);
        notificationService.notifyGiveASectorCreatorAward(sectorCreatorAward);
    }
}
