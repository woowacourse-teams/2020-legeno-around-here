package wooteco.team.ittabi.legenoaroundhere.service.award;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.domain.award.SectorCreatorAward;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.util.AwardNameMaker;
import wooteco.team.ittabi.legenoaroundhere.dto.AwardResponse;
import wooteco.team.ittabi.legenoaroundhere.repository.SectorCreatorAwardRepository;
import wooteco.team.ittabi.legenoaroundhere.service.NotificationService;

@Slf4j
@Service
@AllArgsConstructor
class SectorAwardService {

    private final SectorCreatorAwardRepository sectorCreatorAwardRepository;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    List<AwardResponse> findSectorCreatorAwards(Long awardeeId) {
        return sectorCreatorAwardRepository.findAllByAwardee_Id(awardeeId)
            .stream()
            .map(AwardResponse::of)
            .collect(Collectors.toList());
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
