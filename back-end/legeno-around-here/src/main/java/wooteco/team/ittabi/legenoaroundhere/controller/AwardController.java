package wooteco.team.ittabi.legenoaroundhere.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private final static List<AwardResponse> awards = new ArrayList<>();

    static {
        awards.add(
            AwardResponse.of("일간 송파구 피아니스트 No.1", "피아노를 잘치는 사람", "2020.08.11.", LocalDateTime.now(),
                "/posts/1"));
        awards.add(
            AwardResponse.of("주간 잠실동 개발자 No.10", "개발 나보다 잘 해?", "2020.08.02. ~ 2020.08.09.",
                LocalDateTime.now(), "/posts/1"));
        awards.add(AwardResponse
            .of("월간 서울시 오징어잡이 배 No.4", "야근을 많이 했어요 ㅠ_ㅠ", "2020.08.02. ~ 2020.08.09.",
                LocalDateTime.now(), "/posts/1"));
        awards.add(
            AwardResponse.of("연간 대한민국 잉여 No.2", "내가 바로 잉여다", "2020.08.02. ~ 2020.08.09.",
                LocalDateTime.now(), "/posts/1"));
        awards.add(AwardResponse
            .of("월간 경기도 성남시 웃긴 썰 No.3", "재밌는 썰 소개해봄ㅋㅋ", "2020.07.01. ~ 2020.07.31.",
                LocalDateTime.now(), "/posts/1"));
        awards
            .add(AwardResponse
                .of("웃긴 썰 부문 창시자", "재밌는 썰 소개해봄ㅋㅋ", "", LocalDateTime.now(), "/sector/1"));
        awards.add(AwardResponse
            .of("귀여운 고양이 컨테스트 부문 창시자", "우리집 고양이님이 채고시다!", "", LocalDateTime.now(), "/sector/1"));
        awards.add(
            AwardResponse
                .of("성적표 자랑 부문 창시자", "임펙트있는 성적표 가즈아~", "", LocalDateTime.now(), "/sector/1"));
        awards
            .add(AwardResponse
                .of("이따비팀 화이팅 부문 창시자", "이따비팀 화이팅", "", LocalDateTime.now(), "/sector/1"));
    }

    private final AwardService awardService;

    @GetMapping("/users/{id}/awards")
    public ResponseEntity<List<AwardResponse>> findAwards(@PathVariable Long id) {
        List<AwardResponse> awards = awardService.findAwards(id);

        return ResponseEntity
            .ok(this.awards);
    }

    @GetMapping("/awards/me")
    public ResponseEntity<List<AwardResponse>> findMyAwards() {
        List<AwardResponse> awards = awardService.findMyAwards();

        return ResponseEntity
            .ok(this.awards);
    }
}
