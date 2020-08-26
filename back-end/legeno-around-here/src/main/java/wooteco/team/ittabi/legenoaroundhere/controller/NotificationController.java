package wooteco.team.ittabi.legenoaroundhere.controller;

import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.ME_PATH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.NOTICE_PATH;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.NotificationResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.service.NotificationService;

@RestController
@RequestMapping(NOTICE_PATH)
@AllArgsConstructor
public class NotificationController {

    private final static List<NotificationResponse> myNotifications = new ArrayList<>();

    private final NotificationService notificationService;

    static {
        myNotifications
            .add(NotificationResponse.of(1L, "신청한 부문이 승인되었어요", "/sector/1", Boolean.FALSE));
        myNotifications
            .add(NotificationResponse.of(2L, "신청한 부문이 반려되었어요", "/sector/1", Boolean.FALSE));
        myNotifications.add(NotificationResponse.of(3L, "상을 수상했어요.", "/users/me", Boolean.FALSE));
    }

    @GetMapping(ME_PATH)
    public ResponseEntity<List<NotificationResponse>> findMyNotices() {
        List<NotificationResponse> notificationResponses = notificationService.findMyNotice();

        notificationResponses.addAll(myNotifications);
        return ResponseEntity
            .ok(notificationResponses);
    }

    @PutMapping("/{noticeId}/read")
    public ResponseEntity<Void> readMyNotices(@PathVariable Long noticeId) {
        NotificationResponse myNotifications = NotificationController.myNotifications.stream()
            .filter(myNotification -> myNotification.getId().equals(noticeId))
            .findFirst()
            .orElseThrow(
                () -> new NotExistsException("ID : " + noticeId + " 에 해당하는 Notice가 없습니다!"));

        myNotifications.setRead(Boolean.TRUE);

        return ResponseEntity
            .noContent()
            .build();
    }
}
