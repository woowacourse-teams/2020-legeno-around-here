package wooteco.team.ittabi.legenoaroundhere.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.service.SectorService;

@RestController
@RequestMapping("/sectors")
@AllArgsConstructor
public class SectorController {

    private SectorService sectorService;

    private Map<Long, SectorResponse> sectors = new HashMap<>();

    @PostMapping
    public ResponseEntity<Void> createSector(@RequestBody SectorRequest sectorRequest) {
        Long id = sectors.size() + 1L;
        sectors.put(id,
            new SectorResponse(id, sectorRequest.getName(), sectorRequest.getDescription(),
                UserResponse.from(
                    (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
                )));
        return ResponseEntity
            .created(URI.create("/sectors/" + id))
            .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SectorResponse> findSector(@PathVariable Long id) {
        return ResponseEntity
            .ok(sectors.get(id));
    }

    @GetMapping
    public ResponseEntity<List<SectorResponse>> findAllSector() {
        return ResponseEntity
            .ok(new ArrayList<>(sectors.values()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSector(@PathVariable Long id,
        @RequestBody SectorRequest sectorRequest) {
        sectors.put(id,
            new SectorResponse(id, sectorRequest.getName(), sectorRequest.getDescription(),
                UserResponse.from(
                    (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
                )));

        return ResponseEntity
            .ok()
            .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSector(@PathVariable Long id) {
        sectors.remove(id);

        return ResponseEntity
            .noContent()
            .build();
    }
}
