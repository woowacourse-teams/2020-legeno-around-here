package wooteco.team.ittabi.legenoaroundhere.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.PostReportResponse;
import wooteco.team.ittabi.legenoaroundhere.service.ReportService;

@RestController
@RequestMapping("/admin/reports")
@AllArgsConstructor
public class ReportAdminController {

    private final ReportService reportService;

    @GetMapping("/{id}")
    public ResponseEntity<PostReportResponse> findPostReport(@PathVariable Long id) {
        PostReportResponse postReportResponse = reportService.findPostReport(id);

        return ResponseEntity
            .ok(postReportResponse);
    }

//    @GetMapping
//    public ResponseEntity<Page<AdminSectorResponse>> findAllSector(PageRequest pageRequest) {
//        Page<AdminSectorResponse> sectors
//            = sectorService.findAllSector(PageableAssembler.assemble(pageRequest));
//
//        return ResponseEntity
//            .ok(sectors);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteSector(@PathVariable Long id) {
//        sectorService.deleteSector(id);
//
//        return ResponseEntity
//            .noContent()
//            .build();
//    }
}
