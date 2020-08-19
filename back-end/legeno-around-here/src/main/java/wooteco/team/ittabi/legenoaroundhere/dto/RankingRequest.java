package wooteco.team.ittabi.legenoaroundhere.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.PostSearch;

@AllArgsConstructor
@Setter
@EqualsAndHashCode
@ToString
public class RankingRequest {

    private String areaIds;
    private String sectorIds;
}
