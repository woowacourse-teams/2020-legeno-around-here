package wooteco.team.ittabi.legenoaroundhere.controller;

import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.AWARDS_PATH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.AWARDS_PATH_WITH_SLASH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.ME_PATH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.USERS_PATH_WITH_SLASH;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.AwardResponse;
import wooteco.team.ittabi.legenoaroundhere.service.AwardService;

@RestController
@AllArgsConstructor
public class AwardController {

    private final AwardService awardService;

    @GetMapping(USERS_PATH_WITH_SLASH + "{userId}" + AWARDS_PATH)
    public ResponseEntity<List<AwardResponse>> findAwards(@PathVariable Long userId) {
        List<AwardResponse> awards = awardService.findAwards(userId);

        return ResponseEntity
            .ok(awards);
    }

    @GetMapping(AWARDS_PATH_WITH_SLASH + ME_PATH)
    public ResponseEntity<List<AwardResponse>> findMyAwards() {
        List<AwardResponse> awards = awardService.findMyAwards();

        return ResponseEntity
            .ok(awards);
    }
}
