package wooteco.team.ittabi.legenoaroundhere.dto;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Setter
@EqualsAndHashCode
@ToString
public class PostFilterRequest {

    private String areaIds;
    private String sectorIds;

    public boolean isFilterNothing() {
        return isNullOrEmpty(areaIds)
            && (isNullOrEmpty(sectorIds));
    }

    private boolean isNullOrEmpty(String areas) {
        return Objects.isNull(areas) || areas.isEmpty();
    }

    public boolean isAreaFilter() {
        return !isNullOrEmpty(areaIds) && isNullOrEmpty(sectorIds);
    }

    public boolean isSectorFilter() {
        return isNullOrEmpty(areaIds) && !isNullOrEmpty(sectorIds);
    }

    public List<Long> getAreaIds() {
        return makeIds(areaIds);
    }

    private List<Long> makeIds(String ids) {
        try {
            return Arrays.stream(ids.split(","))
                .map(Long::valueOf)
                .distinct()
                .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<Long> getSectorIds() {
        return makeIds(sectorIds);
    }
}
