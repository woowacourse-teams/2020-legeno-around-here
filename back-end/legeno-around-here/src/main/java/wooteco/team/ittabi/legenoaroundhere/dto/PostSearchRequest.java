package wooteco.team.ittabi.legenoaroundhere.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.PostSearch;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PostSearchRequest {

    private Long areaId;
    private String sectorIds;

    public PostSearch toPostSearch() {
        return PostSearch.builder()
            .areaId(areaId)
            .sectorIds(sectorIds)
            .build();
    }
}
