package wooteco.team.ittabi.legenoaroundhere.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.PostSearchFilter;

@AllArgsConstructor
@Setter
@EqualsAndHashCode
@ToString
public class PostSearchFilterRequest {

    private String areaIds;
    private String sectorIds;

    public PostSearchFilter toPostSearchFilter() {
        return PostSearchFilter.builder()
            .areaIds(areaIds)
            .sectorIds(sectorIds)
            .build();
    }
}
