package wooteco.team.ittabi.legenoaroundhere.controller;

import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.COMMENTS_PATH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.COMMENTS_PATH_WITH_SLASH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.POSTS_PATH_WITH_SLASH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.ZZANGS_PATH;

import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentResponse;
import wooteco.team.ittabi.legenoaroundhere.service.CommentService;

@RestController
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping(POSTS_PATH_WITH_SLASH + "{postId}" + COMMENTS_PATH)
    public ResponseEntity<Void> createComment(@PathVariable Long postId,
        @RequestBody CommentRequest commentRequest) {
        CommentResponse commentResponse = commentService.createComment(postId, commentRequest);

        return ResponseEntity
            .created(URI.create(COMMENTS_PATH_WITH_SLASH + commentResponse.getId()))
            .build();
    }

    @PostMapping(POSTS_PATH_WITH_SLASH + "{postId}" + COMMENTS_PATH_WITH_SLASH + "{commentId}"
        + COMMENTS_PATH)
    public ResponseEntity<Void> createCocomment(@PathVariable Long commentId,
        @RequestBody CommentRequest commentRequest) {
        CommentResponse commentResponse = commentService.createCocomment(commentId, commentRequest);

        return ResponseEntity
            .created(URI.create(COMMENTS_PATH_WITH_SLASH + commentResponse.getId()))
            .build();
    }

    @PutMapping(COMMENTS_PATH_WITH_SLASH + "{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId,
        @RequestBody CommentRequest commentRequest) {
        CommentResponse commentResponse = commentService.updateComment(commentId, commentRequest);

        return ResponseEntity
            .ok()
            .body(commentResponse);
    }

    @GetMapping(COMMENTS_PATH_WITH_SLASH + "{commentId}")
    public ResponseEntity<CommentResponse> findComment(@PathVariable Long commentId) {
        CommentResponse commentResponse = commentService.findComment(commentId);

        return ResponseEntity
            .ok()
            .body(commentResponse);
    }

    @GetMapping(POSTS_PATH_WITH_SLASH + "{postId}" + COMMENTS_PATH)
    public ResponseEntity<List<CommentResponse>> findAllComment(@PathVariable Long postId) {
        List<CommentResponse> commentResponses = commentService.findAllComment(postId);

        return ResponseEntity
            .ok()
            .body(commentResponses);
    }

    @DeleteMapping(COMMENTS_PATH_WITH_SLASH + "{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);

        return ResponseEntity
            .noContent()
            .build();
    }

    @PostMapping(COMMENTS_PATH_WITH_SLASH + "{commentId}" + ZZANGS_PATH)
    public ResponseEntity<Void> pressCommentZzang(@PathVariable Long commentId) {
        commentService.pressZzang(commentId);

        return ResponseEntity
            .noContent()
            .build();
    }
}
