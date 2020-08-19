package wooteco.team.ittabi.legenoaroundhere.controller;

import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.PageRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PageableAssembler;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorDetailResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.service.SectorService;

@RestController
@RequestMapping("/sectors")
@AllArgsConstructor
public class SectorController {

    private final SectorService sectorService;

    @GetMapping("/{id}")
    public ResponseEntity<SectorResponse> findAvailableSector(@PathVariable Long id) {
        SectorResponse sector = sectorService.findAvailableSector(id);

        return ResponseEntity
            .ok(sector);
    }

    @GetMapping
    public ResponseEntity<Page<SectorResponse>> searchAvailableSectors(PageRequest pageRequest,
        @RequestParam(defaultValue = "") String keyword) {
        Page<SectorResponse> sectors = sectorService
            .searchAvailableSectors(PageableAssembler.assemble(pageRequest), keyword);

        return ResponseEntity
            .ok(sectors);
    }

    @PostMapping
    public ResponseEntity<Void> createPendingSector(@RequestBody SectorRequest sectorRequest) {
        SectorResponse sectorResponse = sectorService.createPendingSector(sectorRequest);
        Long id = sectorResponse.getId();

        return ResponseEntity
            .created(URI.create("/sectors/" + id))
            .build();
    }

    @GetMapping("/me")
    public ResponseEntity<Page<SectorDetailResponse>> findAllMySector(PageRequest pageRequest) {
        Page<SectorDetailResponse> sectorDetailResponses
            = sectorService.findAllMySector(PageableAssembler.assemble(pageRequest));

        return ResponseEntity
            .ok(sectorDetailResponses);
    }

    @GetMapping("/best")
    public ResponseEntity<List<SectorResponse>> findBestSectors(
        @RequestParam(defaultValue = "4") int count) {
        List<SectorResponse> sectorResponses
            = sectorService.findBestSectors(count);

        return ResponseEntity
            .ok(sectorResponses);
    }
}