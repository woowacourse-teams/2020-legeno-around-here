package wooteco.team.ittabi.legenoaroundhere.controller;

import java.util.Arrays;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    private final Environment env;

    public WebController(Environment env) {
        this.env = env;
    }

    @GetMapping("/profile")
    public String getProfile() {
        return Arrays.stream(env.getActiveProfiles())
            .findFirst()
            .orElse("none");
    }
}
