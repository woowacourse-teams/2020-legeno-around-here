package wooteco.team.ittabi.legenoaroundhere.controller;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ProfileController {

    private static final String PROFILE_NONE = "none";

    private final Environment env;

    @GetMapping("/profile")
    public String getProfile() {
        return Arrays.stream(env.getActiveProfiles())
            .findFirst()
            .orElse(PROFILE_NONE);
    }
}
