package wooteco.team.ittabi.legenoaroundhere.scheduler;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wooteco.team.ittabi.legenoaroundhere.service.award.AwardService;

@Slf4j
@AllArgsConstructor
@Component
public class PostAwardCreateScheduler {

    private final AwardService awardService;

    @Scheduled(cron = "0 0 0 1 * *")    // cron : 0초 0시 0분 1일 *월 *요일 (매월 1일 0시 0분 0초)
    public void createPopularPostAwards() {
        try {
            awardService.createPopularPostAwards(LocalDateTime.now());
        } catch (Exception e) {
            log.info("Error : ", e);
        }
    }
}
