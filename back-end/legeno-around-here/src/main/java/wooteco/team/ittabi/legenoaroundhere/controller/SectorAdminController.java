package wooteco.team.ittabi.legenoaroundhere.controller;

import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.AdminSectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PageRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorUpdateStateRequest;
import wooteco.team.ittabi.legenoaroundhere.service.SectorService;

@RestController
@RequestMapping("/admin/sectors")
@AllArgsConstructor
public class SectorAdminController {

    private final SectorService sectorService;

    @PostMapping
    public ResponseEntity<Void> createSector(@RequestBody SectorRequest sectorRequest) {
        SectorResponse sector = sectorService.createSector(sectorRequest);
        return ResponseEntity
            .created(URI.create("/sectors/" + sector.getId()))
            .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminSectorResponse> findSector(@PathVariable Long id) {
        AdminSectorResponse sector = sectorService.findSector(id);
        return ResponseEntity
            .ok(sector);
    }

    @GetMapping
    public ResponseEntity<Page<AdminSectorResponse>> findAllSector(PageRequest pageRequest) {
        Page<AdminSectorResponse> sectors = sectorService.findAllSector(pageRequest.getPageable());

        return ResponseEntity
            .ok(sectors);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSector(@PathVariable Long id,
        @RequestBody SectorRequest sectorRequest) {
        sectorService.updateSector(id, sectorRequest);
        return ResponseEntity
            .ok()
            .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSector(@PathVariable Long id) {
        sectorService.deleteSector(id);
        return ResponseEntity
            .noContent()
            .build();
    }

    @PutMapping("/{id}/state")
    public ResponseEntity<Void> updateSectorState(@PathVariable Long id,
        @RequestBody SectorUpdateStateRequest sectorUpdateStateRequest) {
        sectorService.updateSectorState(id, sectorUpdateStateRequest);

        return ResponseEntity
            .ok()
            .build();
    }
}