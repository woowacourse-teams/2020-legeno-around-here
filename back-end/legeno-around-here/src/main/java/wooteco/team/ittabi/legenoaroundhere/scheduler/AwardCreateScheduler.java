package wooteco.team.ittabi.legenoaroundhere.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AwardCreateScheduler {

    @Scheduled(cron = "30 * * * * *")
    public void createPopularityPostCreatorAwards() {
        System.out.println("********");
        System.out.println("하위");
        System.out.println("********");
    }
}
