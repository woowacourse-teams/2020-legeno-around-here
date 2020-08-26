package wooteco.team.ittabi.legenoaroundhere.controller;

import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.RANKING_PATH;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.PageRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PageableAssembler;
import wooteco.team.ittabi.legenoaroundhere.dto.PostSearchRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostWithCommentsCountResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.RankingRequest;
import wooteco.team.ittabi.legenoaroundhere.service.RankingService;

@RestController
@RequestMapping(RANKING_PATH)
@AllArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @GetMapping
    public ResponseEntity<Page<PostWithCommentsCountResponse>> searchRanking(
        PageRequest pageRequest, RankingRequest rankingRequest,
        PostSearchRequest postSearchRequest) {
        Page<PostWithCommentsCountResponse> posts
            = rankingService.searchRanking(PageableAssembler.assembleForRanking(pageRequest),
            postSearchRequest, rankingRequest);

        return ResponseEntity
            .ok()
            .body(posts);
    }
}
