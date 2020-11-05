package wooteco.team.ittabi.legenoaroundhere.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class PostCreateRequest {

    private String writing;
    private List<Long> imageIds;
    private Long areaId;
    private Long sectorId;
}
