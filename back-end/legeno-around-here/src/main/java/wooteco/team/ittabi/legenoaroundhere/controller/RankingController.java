package wooteco.team.ittabi.legenoaroundhere.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.RankingRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.RankingResponse;
import wooteco.team.ittabi.legenoaroundhere.service.RankingService;

@RestController
@RequestMapping("/ranking")
public class RankingController {

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping("/{criteria}")
    public ResponseEntity<RankingResponse> findRanking(@PathVariable String criteria,
        RankingRequest rankingRequest) {
        RankingResponse ranking = rankingService.getRanking(criteria, rankingRequest);
        return ResponseEntity
            .ok()
            .body(ranking);
    }
}
