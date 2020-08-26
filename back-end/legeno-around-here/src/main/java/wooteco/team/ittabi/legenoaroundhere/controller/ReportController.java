package wooteco.team.ittabi.legenoaroundhere.controller;

import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.PostReportCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostReportResponse;
import wooteco.team.ittabi.legenoaroundhere.service.ReportService;

@RestController
@RequestMapping("/reports")
@AllArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/posts/{postId}")
    public ResponseEntity<Void> createPostReport(@PathVariable Long postId, @RequestBody
        PostReportCreateRequest postReportCreateRequest) {
        PostReportResponse postReportResponse = reportService
            .createPostReport(postId, postReportCreateRequest);

        return ResponseEntity
            .created(URI.create("/posts/" + postReportResponse.getId()))
            .build();
    }
}
