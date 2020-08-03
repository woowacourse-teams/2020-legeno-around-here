package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.domain.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotUniqueException;
import wooteco.team.ittabi.legenoaroundhere.repository.SectorRepository;

@Service
@AllArgsConstructor
public class SectorService {

    private final SectorRepository sectorRepository;

    @Transactional
    public SectorResponse createSector(SectorRequest sectorRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            Sector sector = sectorRepository.save(sectorRequest.toSector(user));
            return SectorResponse.of(sector);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new NotUniqueException(
                    "Sector 이름 [" + sectorRequest.getName() + "](이)가 Unique하지 않습니다.");
            }
            throw e;
        }
    }

    public SectorResponse findSector(Long id) {
        Sector sector = findSectorBy(id);
        return SectorResponse.of(sector);
    }

    private Sector findSectorBy(Long id) {
        return sectorRepository.findById(id)
            .orElseThrow(() -> new NotExistsException(id + "에 해당하는 Sector가 존재하지 않습니다."));
    }

    public List<SectorResponse> findAllSector() {
        List<Sector> sectors = sectorRepository.findAll();
        return SectorResponse.listOf(sectors);
    }

    @Transactional
    public void updateSector(Long id, SectorRequest sectorRequest) {
        Sector sector = findSectorBy(id);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        sector.update(sectorRequest.toSector(user));
    }
}
