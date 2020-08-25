package wooteco.team.ittabi.legenoaroundhere.controller;

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
            AwardResponse.of("월간 송파구 피아니스트 부문 1위", "2020.07.01. ~ 2020.07.31.",
                "/posts/1"));
        awards.add(
            AwardResponse.of("월간 잠실동 개발자 부문 1위", "2020.08.02. ~ 2020.08.09.",
                "/posts/1"));
        awards.add(AwardResponse
            .of("월간 서울시 오징어잡이 배 부문 3위", "2020.08.02. ~ 2020.08.09.",
                "/posts/1"));
        awards.add(
            AwardResponse.of("연간 대한민국 잉여 No.2", "2020.08.02. ~ 2020.08.09.",
                "/posts/1"));
        awards.add(AwardResponse
            .of("월간 경기도 성남시 웃긴 썰 No.3", "2020.07.01. ~ 2020.07.31.",
                "/posts/1"));
        awards
            .add(AwardResponse
                .of("웃긴 썰 부문 창시자", "2020.08.30", "/sector/1"));
        awards.add(AwardResponse
            .of("귀여운 고양이 컨테스트 부문 창시자", "2020.08.01", "/sector/1"));
        awards.add(
            AwardResponse
                .of("성적표 자랑 부문 창시자", "2020.08.31", "/sector/1"));
        awards
            .add(AwardResponse
                .of("이따비팀 화이팅 부문 창시자", "2002.01.30", "/sector/1"));
    }

    private final AwardService awardService;

    @GetMapping("/users/{id}/awards")
    public ResponseEntity<List<AwardResponse>> findAwards(@PathVariable Long id) {
        List<AwardResponse> awards = awardService.findAwards(id);

        return ResponseEntity
            .ok(AwardController.awards);
    }

    @GetMapping("/awards/me")
    public ResponseEntity<List<AwardResponse>> findMyAwards() {
        List<AwardResponse> awards = awardService.findMyAwards();

        return ResponseEntity
            .ok(AwardController.awards);
    }
}
