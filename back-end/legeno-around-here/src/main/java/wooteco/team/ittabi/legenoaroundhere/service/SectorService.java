package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.SectorState;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.AdminSectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotUniqueException;
import wooteco.team.ittabi.legenoaroundhere.repository.SectorRepository;

@Service
@AllArgsConstructor
public class SectorService {

    private final SectorRepository sectorRepository;
    private final IAuthenticationFacade authenticationFacade;

    @Transactional
    public SectorResponse createSector(SectorRequest sectorRequest) {
        User user = (User) authenticationFacade.getPrincipal();
        Sector sector = sectorRequest.toSector(user, SectorState.PUBLISHED);
        return saveSector(sector);
    }

    private SectorResponse saveSector(Sector sector) {
        sectorRepository.findByName(sector.getName())
            .ifPresent(foundSector -> {
                throw new NotUniqueException(foundSector.getStateExceptionName() + " 상태의 ["
                    + foundSector.getName() + "] 부문이 이미 존재합니다.");
            });

        Sector savedSector = sectorRepository.save(sector);
        return SectorResponse.of(savedSector);
    }

    public SectorResponse findInUseSector(Long id) {
        Sector sector = findUsedSectorBy(id);
        return SectorResponse.of(sector);
    }

    private Sector findUsedSectorBy(Long id) {
        Sector sector = findSectorBy(id);
        if (sector.isNotUsed()) {
            throw new NotExistsException(id + "에 해당하는 Sector가 존재하지 않습니다.");
        }
        return sector;
    }

    private Sector findSectorBy(Long id) {
        return sectorRepository.findById(id)
            .orElseThrow(() -> new NotExistsException(id + "에 해당하는 Sector가 존재하지 않습니다."));
    }

    public List<SectorResponse> findAllInUseSector() {
        List<Sector> sectors = sectorRepository.findAll()
            .stream()
            .filter(Sector::isUsed)
            .collect(Collectors.toList());

        return SectorResponse.listOf(sectors);
    }

    @Transactional
    public void updateSector(Long id, SectorRequest sectorRequest) {
        Sector sector = findUsedSectorBy(id);

        User user = (User) authenticationFacade.getPrincipal();
        sector.update(sectorRequest.toSector(user, SectorState.PUBLISHED));
    }

    @Transactional
    public void deleteSector(Long id) {
        Sector sector = findUsedSectorBy(id);
        sector.setState(SectorState.DELETED);
    }

    public AdminSectorResponse findSector(Long id) {
        Sector sector = findSectorBy(id);
        return AdminSectorResponse.of(sector);
    }

    public List<AdminSectorResponse> findAllSector() {
        List<Sector> sectors = sectorRepository.findAll();
        return AdminSectorResponse.listOf(sectors);
    }

    public SectorResponse createPendingSector(SectorRequest sectorRequest) {
        User user = (User) authenticationFacade.getPrincipal();
        Sector sector = sectorRequest.toSector(user, SectorState.PENDING);

        return saveSector(sector);
    }
}
