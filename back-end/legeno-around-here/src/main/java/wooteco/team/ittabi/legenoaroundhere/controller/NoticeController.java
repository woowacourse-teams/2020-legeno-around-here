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

@RestController
@RequestMapping(NOTICE_PATH)
@AllArgsConstructor
public class NoticeController {

    private final static List<NotificationResponse> myNotices = new ArrayList<>();

    static {
        myNotices
            .add(NotificationResponse.of(1L, "당신의 글에 5명이 댓글을 달았어요", "/posts/1", Boolean.FALSE));
        myNotices.add(NotificationResponse.of(2L, "당신의 글에 2명이 짱을 눌렀어요", "/posts/1", Boolean.FALSE));
        myNotices
            .add(NotificationResponse.of(3L, "당신의 댓글에 3명이 댓글을 달았어요", "/posts/1", Boolean.FALSE));
        myNotices
            .add(NotificationResponse.of(4L, "당신의 댓글에 8명이 짱을 눌렀어요", "/posts/1", Boolean.FALSE));
        myNotices.add(NotificationResponse.of(5L, "신청한 부문이 승인되었어요", "/sector/1", Boolean.FALSE));
        myNotices.add(NotificationResponse.of(6L, "신청한 부문이 반려되었어요", "/sector/1", Boolean.FALSE));
        myNotices.add(NotificationResponse.of(7L, "상을 수상했어요.", "/users/me", Boolean.FALSE));
        myNotices.add(NotificationResponse.of(8L, "가입을 축하해요.", "/users/me", Boolean.FALSE));
    }

    @GetMapping(ME_PATH)
    public ResponseEntity<List<NotificationResponse>> findMyNotices() {
        return ResponseEntity
            .ok(myNotices);
    }

    @PutMapping("/{noticeId}/read")
    public ResponseEntity<Void> readMyNotices(@PathVariable Long noticeId) {
        NotificationResponse notice = myNotices.stream()
            .filter(myNotice -> myNotice.getId().equals(noticeId))
            .findFirst()
            .orElseThrow(
                () -> new NotExistsException("ID : " + noticeId + " 에 해당하는 Notice가 없습니다!"));

        notice.setRead(Boolean.TRUE);

        return ResponseEntity
            .noContent()
            .build();
    }
}
