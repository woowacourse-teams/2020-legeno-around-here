package wooteco.team.ittabi.legenoaroundhere.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    @PostMapping
    public ResponseEntity<Void> createComment(@PathVariable Long postId,
        @RequestBody CommentRequest commentRequest) {
        return ResponseEntity
            .created(URI.create("/posts/" + postId))
            .build();
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> findComment(@PathVariable Long postId,
        @PathVariable Long commentId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity
            .ok()
            .body(new CommentResponse(1L, "Test Writing!", UserResponse.from(user)));
    }
}
