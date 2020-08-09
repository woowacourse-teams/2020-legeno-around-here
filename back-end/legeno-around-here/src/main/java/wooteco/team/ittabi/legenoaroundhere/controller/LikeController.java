package wooteco.team.ittabi.legenoaroundhere.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.LikeResponse;
import wooteco.team.ittabi.legenoaroundhere.service.LikeService;

@RestController
@RequestMapping("/posts/{postId}/likes")
@AllArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @GetMapping
    public ResponseEntity<LikeResponse> pressLike(@PathVariable Long postId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LikeResponse likeResponse = likeService.pressLike(postId, user);
        return ResponseEntity
            .ok()
            .body(likeResponse);
    }
}
