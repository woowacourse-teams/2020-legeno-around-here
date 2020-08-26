package wooteco.team.ittabi.legenoaroundhere.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.PageRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PageableAssembler;
import wooteco.team.ittabi.legenoaroundhere.dto.PostReportResponse;
import wooteco.team.ittabi.legenoaroundhere.service.ReportService;

@RestController
@RequestMapping("/admin/reports")
@AllArgsConstructor
public class ReportAdminController {

    private final ReportService reportService;

    @GetMapping("/{id}")
    public ResponseEntity<PostReportResponse> findPostReport(@PathVariable Long id) {
        PostReportResponse postReport = reportService.findPostReport(id);

        return ResponseEntity
            .ok(postReport);
    }

    @GetMapping
    public ResponseEntity<Page<PostReportResponse>> findPostReportByPage(PageRequest pageRequest) {
        Page<PostReportResponse> postReports
            = reportService.findPostReportByPage(PageableAssembler.assemble(pageRequest));

        return ResponseEntity
            .ok(postReports);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostReport(@PathVariable Long id) {
        reportService.deletePostReport(id);

        return ResponseEntity
            .noContent()
            .build();
    }
}
