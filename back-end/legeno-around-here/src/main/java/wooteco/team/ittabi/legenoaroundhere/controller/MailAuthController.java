package wooteco.team.ittabi.legenoaroundhere.controller;

import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.MAIL_AUTH_PATH;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.MailAuthCheckRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.MailAuthCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.MailAuthFindPasswordRequest;
import wooteco.team.ittabi.legenoaroundhere.service.MailAuthService;

@RestController
@RequestMapping(MAIL_AUTH_PATH)
@AllArgsConstructor
public class MailAuthController {

    private final MailAuthService mailAuthService;

    @PostMapping("/send")
    public ResponseEntity<Void> sendAuthMail(
        @RequestBody MailAuthCreateRequest mailAuthCreateRequest) {
        mailAuthService.publishAuth(mailAuthCreateRequest);

        return ResponseEntity
            .ok()
            .build();
    }

    @PostMapping("/find/password")
    public ResponseEntity<Void> findPasswordAuthMail(
        @RequestBody MailAuthFindPasswordRequest mailAuthFindPasswordRequest) {
        mailAuthService.publishAuth(mailAuthFindPasswordRequest);

        return ResponseEntity
            .ok()
            .build();
    }

    @PostMapping("/check")
    public ResponseEntity<Void> checkAuth(@RequestBody MailAuthCheckRequest mailAuthCheckRequest) {
        mailAuthService.checkAuth(mailAuthCheckRequest);

        return ResponseEntity
            .ok()
            .build();
    }
}
