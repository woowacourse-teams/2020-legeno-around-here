package wooteco.team.ittabi.legenoaroundhere.controller;

import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.COMMENTS_PATH_WITH_SLASH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.POSTS_PATH_WITH_SLASH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.REPORTS_PATH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.USERS_PATH_WITH_SLASH;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.ReportCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.service.report.CommentReportService;
import wooteco.team.ittabi.legenoaroundhere.service.report.PostReportService;
import wooteco.team.ittabi.legenoaroundhere.service.report.UserReportService;

@RestController
@RequestMapping(REPORTS_PATH)
@AllArgsConstructor
public class ReportController {

    private final PostReportService postReportService;
    private final CommentReportService commentReportService;
    private final UserReportService userReportService;

    @PostMapping(POSTS_PATH_WITH_SLASH + "{postId}")
    public ResponseEntity<Void> createPostReport(@PathVariable Long postId, @RequestBody
        ReportCreateRequest reportCreateRequest) {
        postReportService.createPostReport(postId, reportCreateRequest);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }

    @PostMapping(COMMENTS_PATH_WITH_SLASH + "{commentId}")
    public ResponseEntity<Void> createCommentReport(@PathVariable Long commentId, @RequestBody
        ReportCreateRequest reportCreateRequest) {
        commentReportService.createCommentReport(commentId, reportCreateRequest);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }

    @PostMapping(USERS_PATH_WITH_SLASH + "{userId}")
    public ResponseEntity<Void> createUserReport(@PathVariable Long userId, @RequestBody
        ReportCreateRequest reportCreateRequest) {
        userReportService.createUserReport(userId, reportCreateRequest);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }
}
