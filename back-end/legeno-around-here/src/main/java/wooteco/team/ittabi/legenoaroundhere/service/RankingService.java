package wooteco.team.ittabi.legenoaroundhere.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.dto.RankingRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.RankingResponse;

@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class RankingService {

    public RankingResponse getRanking(String criteria, RankingRequest rankingRequest) {
        return null;
    }
}
