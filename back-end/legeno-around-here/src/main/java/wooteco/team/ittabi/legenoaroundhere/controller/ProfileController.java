package wooteco.team.ittabi.legenoaroundhere.controller;

import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.PROFILE_PATH;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PROFILE_PATH)
@AllArgsConstructor
public class ProfileController {

    private static final String PROFILE_NONE = "none";

    private final Environment env;

    @GetMapping
    public String getProfile() {
        return Arrays.stream(env.getDefaultProfiles())
            .findFirst()
            .orElse(PROFILE_NONE);
    }
}
