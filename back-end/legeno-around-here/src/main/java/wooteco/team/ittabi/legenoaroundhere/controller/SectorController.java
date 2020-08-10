package wooteco.team.ittabi.legenoaroundhere.controller;

import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorDetailResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.service.SectorService;

@RestController
@AllArgsConstructor
public class SectorController {

    private final SectorService sectorService;

    @GetMapping("/sectors/{id}")
    public ResponseEntity<SectorResponse> findInUseSector(@PathVariable Long id) {
        SectorResponse sector = sectorService.findInUseSector(id);
        return ResponseEntity
            .ok(sector);
    }

    @GetMapping("/sectors")
    public ResponseEntity<List<SectorResponse>> findAllInUseSector() {
        List<SectorResponse> sectors = sectorService.findAllInUseSector();
        return ResponseEntity
            .ok(sectors);
    }

    @PostMapping("/sectors")
    public ResponseEntity<Void> createPendingSector(@RequestBody SectorRequest sectorRequest) {
        SectorResponse sectorResponse = sectorService.createPendingSector(sectorRequest);
        Long id = sectorResponse.getId();
        return ResponseEntity
            .created(URI.create("/sectors/" + id))
            .build();
    }

    @GetMapping("/sectors/me")
    public ResponseEntity<List<SectorDetailResponse>> findAllMySector() {
        List<SectorDetailResponse> sectorDetailResponses = sectorService.findAllMySector();

        return ResponseEntity
            .ok(sectorDetailResponses);
    }
}