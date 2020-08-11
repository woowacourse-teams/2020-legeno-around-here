package wooteco.team.ittabi.legenoaroundhere.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.LikeResponse;
import wooteco.team.ittabi.legenoaroundhere.service.LikeService;

@RestController
@RequestMapping("/posts/{postId}/likes")
@AllArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @GetMapping
    public ResponseEntity<LikeResponse> pressLike(@PathVariable Long postId) {
        LikeResponse likeResponse = likeService.pressLike(postId);
        return ResponseEntity
            .ok()
            .body(likeResponse);
    }
}
