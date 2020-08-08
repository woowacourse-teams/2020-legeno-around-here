package wooteco.team.ittabi.legenoaroundhere.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.SectorState;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorDetailResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorUpdateStateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotUniqueException;
import wooteco.team.ittabi.legenoaroundhere.service.SectorService;

@RestController
@AllArgsConstructor
public class SectorController {

    private final SectorService sectorService;

    private Map<Long, SectorDetailResponse> sectorDetailResponses;

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
        validate(sectorRequest);
        SectorResponse sectorResponse = sectorService.createPendingSector(sectorRequest);
        Long id = sectorResponse.getId();
        id = sectorDetailResponses.size() + 1L;
        sectorDetailResponses.put(id, SectorDetailResponse.builder()
            .id(id)
            .name(sectorRequest.getName())
            .description(sectorRequest.getDescription())
            .creator(UserResponse
                .from((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()))
            .state(SectorState.PENDING.getName())
            .reason("")
            .build());
        return ResponseEntity
            .created(URI.create("/sectors/" + id))
            .build();
    }

    private void validate(SectorRequest sectorRequest) {
        String name = sectorRequest.getName();
        sectorDetailResponses.values().stream()
            .filter(sectorDetailResponse -> sectorDetailResponse.getName().equals(name))
            .peek(System.out::println)
            .findFirst()
            .ifPresent(sectorDetailResponse -> throwException(sectorDetailResponse.getState()));
    }

    private void throwException(String state) {
        if (state.equals(SectorState.APPROVED.getName())) {
            throw new NotUniqueException("사용 부문");
        }
        if (state.equals(SectorState.PENDING.getName())) {
            throw new NotUniqueException("승인 신청 부문");
        }
        if (state.equals(SectorState.REJECTED.getName())) {
            throw new NotUniqueException("반려 부문");
        }
        if (state.equals(SectorState.PUBLISHED.getName())) {
            throw new NotUniqueException("사용 부문");
        }
    }

    @GetMapping("/my/sectors")
    public ResponseEntity<List<SectorDetailResponse>> findMySectors() {

        return ResponseEntity
            .ok(new ArrayList<>(sectorDetailResponses.values()));
    }

    @PutMapping("/admin/sectors/{id}/state")
    public ResponseEntity<Void> updateSectorState(@PathVariable Long id,
        @RequestBody SectorUpdateStateRequest sectorUpdateStateRequest) {

        SectorDetailResponse sectorDetailResponse = sectorDetailResponses.get(id);

        String name = sectorDetailResponse.getName();
        if (sectorUpdateStateRequest.getState().equals("반려")) {
            name += "_REJECTED";
        }
        sectorDetailResponses.put(id, SectorDetailResponse.builder()
            .id(sectorDetailResponse.getId())
            .name(name)
            .description(sectorDetailResponse.getDescription())
            .creator(sectorDetailResponse.getCreator())
            .state(sectorUpdateStateRequest.getState())
            .reason(sectorUpdateStateRequest.getReason())
            .build());

        return ResponseEntity
            .ok()
            .build();
    }
}