package wooteco.team.ittabi.legenoaroundhere.controller;

import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentResponse;
import wooteco.team.ittabi.legenoaroundhere.service.CommentService;

@RestController
@AllArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@PathVariable Long postId,
        @RequestBody CommentRequest commentRequest) {
        CommentResponse commentResponse = commentService.createComment(postId, commentRequest);
        return ResponseEntity
            .created(URI.create("/posts/" + postId + "/comments/" + commentResponse.getId()))
            .build();
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> findComment(@PathVariable Long commentId) {
        CommentResponse commentResponse = commentService.findComment(commentId);
        return ResponseEntity
            .ok()
            .body(commentResponse);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> findAllComment(@PathVariable Long postId) {
        List<CommentResponse> commentResponses = commentService.findAllComment(postId);
        return ResponseEntity
            .ok()
            .body(commentResponses);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity
            .noContent()
            .build();
    }
}
