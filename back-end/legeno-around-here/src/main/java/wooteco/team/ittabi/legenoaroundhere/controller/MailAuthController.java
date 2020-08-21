package wooteco.team.ittabi.legenoaroundhere.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.MailAuthCheckRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.MailAuthCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.service.MailAuthService;

@RestController
@RequestMapping("/mail-auth")
public class MailAuthController {

    private final MailAuthService mailAuthService;

    public MailAuthController(MailAuthService mailAuthService) {
        this.mailAuthService = mailAuthService;
    }

    @PostMapping("/send")
    public ResponseEntity<Void> mailAuth(@RequestBody MailAuthCreateRequest mailAuthCreateRequest) {
        mailAuthService.publishAuth(mailAuthCreateRequest);

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
