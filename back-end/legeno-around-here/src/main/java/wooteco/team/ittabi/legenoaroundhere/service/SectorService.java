package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Name;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.SectorState;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.AdminSectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorDetailResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorUpdateStateRequest;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotUniqueException;
import wooteco.team.ittabi.legenoaroundhere.repository.SectorRepository;

@Service
@AllArgsConstructor
public class SectorService {

    private static final String DB_LIKE_FORMAT = "%%%s%%";
    private final SectorRepository sectorRepository;
    private final IAuthenticationFacade authenticationFacade;

    @Transactional
    public SectorResponse createSector(SectorRequest sectorRequest) {
        User user = (User) authenticationFacade.getPrincipal();
        Sector sector = sectorRequest.toSector(user, SectorState.PUBLISHED);

        validateSectorUnique(sector);
        return saveSector(sector);
    }

    private void validateSectorUnique(Sector sector) {
        List<Sector> sectors = sectorRepository.findAllByName(sector.getName());
        sectors.remove(sector);

        sectors.stream()
            .filter(Sector::isUniqueState)
            .findFirst()
            .ifPresent(foundSector -> {
                throw new NotUniqueException(foundSector.getStateExceptionName() + " 상태의 ["
                    + foundSector.getName() + "] 부문이 이미 존재합니다.");
            });
    }

    private SectorResponse saveSector(Sector sector) {
        Sector savedSector = sectorRepository.save(sector);
        return SectorResponse.of(savedSector);
    }

    @Transactional(readOnly = true)
    public SectorResponse findAvailableSector(Long id) {
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

    @Transactional(readOnly = true)
    public Page<SectorResponse> searchAvailableSectors(Pageable pageable, String keyword) {
        String likeKeyword = String.format(DB_LIKE_FORMAT, keyword);
        Page<Sector> sectorsPage = sectorRepository.findAllByStateInAndNameIsLike(pageable,
            SectorState.getAllAvailable(), Name.getKeywordName(likeKeyword));
        return sectorsPage.map(SectorResponse::of);
    }

    @Transactional
    public void updateSector(Long id, SectorRequest sectorRequest) {
        Sector sector = findUsedSectorBy(id);

        User user = (User) authenticationFacade.getPrincipal();
        sector.update(sectorRequest.toSector(user, SectorState.PUBLISHED));
    }

    @Transactional
    public void deleteSector(Long id) {
        User user = (User) authenticationFacade.getPrincipal();

        Sector sector = findUsedSectorBy(id);
        sector.setState(SectorState.DELETED, "삭제", user);
    }

    @Transactional(readOnly = true)
    public AdminSectorResponse findSector(Long id) {
        Sector sector = findSectorBy(id);
        return AdminSectorResponse.of(sector);
    }

    @Transactional(readOnly = true)
    public Page<AdminSectorResponse> findAllSector(Pageable pageable) {
        Page<Sector> sectorsPage = sectorRepository.findAll(pageable);
        return sectorsPage.map(AdminSectorResponse::of);
    }

    @Transactional
    public SectorResponse createPendingSector(SectorRequest sectorRequest) {
        User user = (User) authenticationFacade.getPrincipal();
        Sector sector = sectorRequest.toSector(user, SectorState.PENDING);

        validateSectorUnique(sector);
        return saveSector(sector);
    }

    @Transactional(readOnly = true)
    public Page<SectorDetailResponse> findAllMySector(Pageable pageable) {
        User user = (User) authenticationFacade.getPrincipal();
        Page<Sector> sectors = sectorRepository.findAllByCreator(pageable, user);
        return sectors.map(SectorDetailResponse::of);
    }

    @Transactional
    public void updateSectorState(Long id, SectorUpdateStateRequest sectorUpdateStateRequest) {
        User user = (User) authenticationFacade.getPrincipal();
        Sector sector = findSectorBy(id);

        validateSectorUnique(sector);
        sector.setState(sectorUpdateStateRequest.getSectorState(),
            sectorUpdateStateRequest.getReason(), user);
    }
}
