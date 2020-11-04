package wooteco.team.ittabi.legenoaroundhere.controller;

import static wooteco.team.ittabi.legenoaroundhere.util.UrlPathConstants.COMMENT_REPORTS;
import static wooteco.team.ittabi.legenoaroundhere.util.UrlPathConstants.POST_REPORTS;
import static wooteco.team.ittabi.legenoaroundhere.util.UrlPathConstants.USER_REPORTS;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.ReportCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.service.report.CommentReportService;
import wooteco.team.ittabi.legenoaroundhere.service.report.PostReportService;
import wooteco.team.ittabi.legenoaroundhere.service.report.UserReportService;

@RestController
@AllArgsConstructor
public class ReportController {

    private final PostReportService postReportService;
    private final CommentReportService commentReportService;
    private final UserReportService userReportService;

    @PostMapping(POST_REPORTS)
    public ResponseEntity<Void> createPostReport(
        @RequestBody ReportCreateRequest reportCreateRequest) {
        postReportService.createPostReport(reportCreateRequest);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }

    @PostMapping(COMMENT_REPORTS)
    public ResponseEntity<Void> createCommentReport(
        @RequestBody ReportCreateRequest reportCreateRequest) {
        commentReportService.createCommentReport(reportCreateRequest);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }

    @PostMapping(USER_REPORTS)
    public ResponseEntity<Void> createUserReport(
        @RequestBody ReportCreateRequest reportCreateRequest) {
        userReportService.createUserReport(reportCreateRequest);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }
}
