package wooteco.team.ittabi.legenoaroundhere.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.PostSearch;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class RankingRequest {

    private String criteria;
}
