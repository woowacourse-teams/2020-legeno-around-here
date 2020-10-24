package wooteco.team.ittabi.legenoaroundhere.controller;

import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.PROFILE_PATH;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PROFILE_PATH)
@AllArgsConstructor
public class ProfileController {

    @Value("${spring.profiles.active}")
    private String profile;

    @GetMapping
    public String getProfile() {
        return profile;
    }
}
