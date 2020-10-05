package wooteco.team.ittabi.legenoaroundhere.scheduler;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wooteco.team.ittabi.legenoaroundhere.service.award.PopularPostAwardService;

@AllArgsConstructor
@Component
public class PostAwardCreateScheduler {

    private PopularPostAwardService popularPostAwardService;

    @Scheduled(cron = "0 0 0 1 * *")    // cron : 0초 0시 0분 1일 *월 *요일 (매월 1일 0시 0분 0초)
    public void createPopularPostAwards() {
        popularPostAwardService.createPopularPostAwards(LocalDateTime.now());
    }
}
