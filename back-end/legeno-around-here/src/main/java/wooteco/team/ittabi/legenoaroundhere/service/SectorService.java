package wooteco.team.ittabi.legenoaroundhere.service;

import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
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
        Sector sector = sectorRepository.findById(id)
            .orElseThrow(() -> new NotExistsException(id + "에 해당하는 Sector가 존재하지 않습니다."));
        return SectorResponse.of(sector);
    }
}
