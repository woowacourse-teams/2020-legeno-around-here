package wooteco.team.ittabi.legenoaroundhere.service;

import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        try {
            Sector sector = sectorRepository
                .save(sectorRequest.toSector(user, SectorState.PUBLISHED));
            return SectorResponse.of(sector);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new NotUniqueException(
                    "Sector 이름 [" + sectorRequest.getName() + "](이)가 Unique하지 않습니다.");
            }
            throw e;
        }
    }

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

    public Page<SectorResponse> findAllAvailableSector(Pageable pageable) {
        Page<Sector> sectorsPage = sectorRepository.findAllByStateIn(pageable,
            SectorState.getAllAvailable());

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
        Sector sector = findUsedSectorBy(id);
        sector.setState(SectorState.DELETED);
    }

    public AdminSectorResponse findSector(Long id) {
        Sector sector = findSectorBy(id);
        return AdminSectorResponse.of(sector);
    }

    public Page<AdminSectorResponse> findAllSector(Pageable pageable) {
        Page<Sector> sectorsPage = sectorRepository.findAll(pageable);
        return sectorsPage.map(AdminSectorResponse::of);
    }
}
