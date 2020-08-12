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
public class PostSearchRequest {

    private String areaIds;
    private String sectorIds;

    public PostSearch toPostSearch() {
        return PostSearch.builder()
            .areaIds(areaIds)
            .sectorIds(sectorIds)
            .build();
    }
}
