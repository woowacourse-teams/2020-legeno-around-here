package wooteco.team.ittabi.legenoaroundhere.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorWithReasonResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.service.SectorService;

@RestController
@AllArgsConstructor
public class SectorController {

    private final SectorService sectorService;

    private Map<Long, SectorResponse> sectorResponses;

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
        Long id = sectorResponses.size() + 1L;
        sectorResponses.put(id, SectorResponse.builder()
            .id(id)
            .name(sectorRequest.getName())
            .description(sectorRequest.getDescription())
            .creator(UserResponse
                .from((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()))
            .build());
        return ResponseEntity
            .created(URI.create("/sectors/1"))
            .build();
    }

    @GetMapping("/my/sectors")
    public ResponseEntity<List<SectorWithReasonResponse>> findMySectors() {
        List<SectorWithReasonResponse> responses = sectorResponses.values()
            .stream()
            .map(sectorResponse -> SectorWithReasonResponse.builder()
                .id(sectorResponse.getId())
                .name(sectorResponse.getName())
                .description(sectorResponse.getDescription())
                .reason("부적합한 부문명")
                .creator(sectorResponse.getCreator())
                .build())
            .collect(Collectors.toList());

        return ResponseEntity
            .ok(responses);
    }
}