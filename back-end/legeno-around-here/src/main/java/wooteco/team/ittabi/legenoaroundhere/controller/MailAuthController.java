package wooteco.team.ittabi.legenoaroundhere.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.MailAuthCheckRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.MailAuthCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.service.MailAuthService;

@RestController
@RequestMapping("/mail-auth")
@AllArgsConstructor
public class MailAuthController {

    private final MailAuthService mailAuthService;

    @PostMapping("/send")
    public ResponseEntity<Void> mailAuth(@RequestBody MailAuthCreateRequest mailAuthCreateRequest) {
        mailAuthService.publishAuth(mailAuthCreateRequest);

        return ResponseEntity
            .ok()
            .build();
    }

    @GetMapping("/check")
    public String checkAuth(MailAuthCheckRequest mailAuthCheckRequest) {
        mailAuthService.checkAuth(mailAuthCheckRequest);

        return "메일 인증을 완료했습니다. 창을 닫아주세요!";
    }
}
