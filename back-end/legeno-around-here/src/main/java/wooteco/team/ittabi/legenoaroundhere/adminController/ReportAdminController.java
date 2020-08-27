package wooteco.team.ittabi.legenoaroundhere.adminController;

import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.ADMIN_PATH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.REPORTS_PATH;

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
import wooteco.team.ittabi.legenoaroundhere.service.report.PostReportService;

@RestController
@RequestMapping(ADMIN_PATH + REPORTS_PATH)
@AllArgsConstructor
public class ReportAdminController {

    private final PostReportService postReportService;

    @GetMapping("/{id}")
    public ResponseEntity<PostReportResponse> findPostReport(@PathVariable Long id) {
        PostReportResponse postReport = postReportService.findPostReport(id);

        return ResponseEntity
            .ok(postReport);
    }

    @GetMapping
    public ResponseEntity<Page<PostReportResponse>> findPostReportByPage(PageRequest pageRequest) {
        Page<PostReportResponse> postReports
            = postReportService.findPostReportByPage(PageableAssembler.assemble(pageRequest));

        return ResponseEntity
            .ok(postReports);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostReport(@PathVariable Long id) {
        postReportService.deletePostReport(id);

        return ResponseEntity
            .noContent()
            .build();
    }
}
