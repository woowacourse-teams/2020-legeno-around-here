package wooteco.team.ittabi.legenoaroundhere.controller;

import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.ME_PATH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.SECTORS_PATH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.SECTORS_PATH_WITH_SLASH;

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
@RequestMapping(SECTORS_PATH)
@AllArgsConstructor
public class SectorController {

    private final SectorService sectorService;

    @GetMapping("/{sectorId}")
    public ResponseEntity<SectorResponse> findAvailableSector(@PathVariable Long sectorId) {
        SectorResponse sector = sectorService.findAvailableSector(sectorId);

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
        Long sectorId = sectorResponse.getId();

        return ResponseEntity
            .created(URI.create(SECTORS_PATH_WITH_SLASH + sectorId))
            .build();
    }

    @GetMapping(ME_PATH)
    public ResponseEntity<Page<SectorDetailResponse>> findMySectors(PageRequest pageRequest) {
        Page<SectorDetailResponse> sectorDetailResponses
            = sectorService.findMySectors(PageableAssembler.assemble(pageRequest));

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