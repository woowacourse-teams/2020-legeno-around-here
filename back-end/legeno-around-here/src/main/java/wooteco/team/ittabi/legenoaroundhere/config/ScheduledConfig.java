package wooteco.team.ittabi.legenoaroundhere.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan("wooteco.team.ittabi.legenoaroundhere.scheduler")
public class ScheduledConfig {
}
