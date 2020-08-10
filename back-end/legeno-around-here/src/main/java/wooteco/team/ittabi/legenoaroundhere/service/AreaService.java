package wooteco.team.ittabi.legenoaroundhere.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.area.AreaRepository;
import wooteco.team.ittabi.legenoaroundhere.dto.AreaResponse;

@Service
@AllArgsConstructor
public class AreaService {

    protected static final String DB_LIKE_CHARACTER = "%";
    private final AreaRepository areaRepository;

    public Page<AreaResponse> searchAllAreaBy(Pageable pageable, String keyword) {
        String likeKeywords = DB_LIKE_CHARACTER + keyword + DB_LIKE_CHARACTER;
        Page<Area> areas = areaRepository.findAllByFullNameIsLike(pageable, likeKeywords);
        return areas.map(AreaResponse::of);
    }
}
