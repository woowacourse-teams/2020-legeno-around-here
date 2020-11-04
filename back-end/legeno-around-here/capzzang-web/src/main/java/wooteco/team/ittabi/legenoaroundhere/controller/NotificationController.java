package wooteco.team.ittabi.legenoaroundhere.controller;

import static wooteco.team.ittabi.legenoaroundhere.util.UrlPathConstants.ME_PATH;
import static wooteco.team.ittabi.legenoaroundhere.util.UrlPathConstants.NOTIFICATION_PATH;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.NotificationResponse;
import wooteco.team.ittabi.legenoaroundhere.service.NotificationService;

@RestController
@RequestMapping(NOTIFICATION_PATH)
@AllArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(ME_PATH)
    public ResponseEntity<List<NotificationResponse>> findMyNotices() {
        List<NotificationResponse> notificationResponses = notificationService.findMyNotice();

        return ResponseEntity
            .ok(notificationResponses);
    }

    @PutMapping("/{noticeId}/read")
    public ResponseEntity<Void> readMyNotice(@PathVariable Long noticeId) {
        notificationService.readMyNotice(noticeId);

        return ResponseEntity
            .noContent()
            .build();
    }
}
