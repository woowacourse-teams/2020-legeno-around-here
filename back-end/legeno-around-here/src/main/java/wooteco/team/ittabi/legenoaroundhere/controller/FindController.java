package wooteco.team.ittabi.legenoaroundhere.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.PasswordFindRequest;
import wooteco.team.ittabi.legenoaroundhere.service.find.PasswordFindService;

@RestController
@RequiredArgsConstructor
public class FindController {

    private final PasswordFindService passwordFindService;

    @PostMapping("/find/password")
    public ResponseEntity<Void> resetPassword(
        @RequestBody PasswordFindRequest passwordFindRequest) {
        passwordFindService.findPassword(passwordFindRequest);

        return ResponseEntity
            .ok()
            .build();
    }
}
