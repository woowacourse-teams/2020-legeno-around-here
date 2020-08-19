package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.AwardResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.AwardResponseAssembler;
import wooteco.team.ittabi.legenoaroundhere.repository.PopularityPostCreatorAwardRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.SectorCreatorAwardRepository;

@Service
@AllArgsConstructor
public class AwardService {

    private final PopularityPostCreatorAwardRepository popularityPostCreatorAwardRepository;
    private final SectorCreatorAwardRepository sectorCreatorAwardRepository;
    private final IAuthenticationFacade authenticationFacade;

    public List<AwardResponse> findAwards(Long awardeeId) {
        List<AwardResponse> awardResponses = new ArrayList<>();
        awardResponses.addAll(findPopularityPostCreatorAwards(awardeeId));
        awardResponses.addAll(findSectorCreatorAwards(awardeeId));

        return awardResponses;
    }

    private List<AwardResponse> findPopularityPostCreatorAwards(Long awardeeId) {
        return popularityPostCreatorAwardRepository.findAllByAwardee_Id(awardeeId)
            .stream()
            .map(AwardResponseAssembler::assemble)
            .collect(Collectors.toList());
    }

    private List<AwardResponse> findSectorCreatorAwards(Long awardeeId) {
        return sectorCreatorAwardRepository.findAllByAwardee_Id(awardeeId)
            .stream()
            .map(AwardResponseAssembler::assemble)
            .collect(Collectors.toList());
    }

    public List<AwardResponse> findMyAwards() {
        User user = (User) authenticationFacade.getPrincipal();

        return findAwards(user.getId());
    }
}
