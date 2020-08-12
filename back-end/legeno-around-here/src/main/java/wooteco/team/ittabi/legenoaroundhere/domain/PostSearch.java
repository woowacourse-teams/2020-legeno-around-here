package wooteco.team.ittabi.legenoaroundhere.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

@Getter
@EqualsAndHashCode
@ToString
public class PostSearch {

    protected static final String DELIMITER = ",";
    private List<Long> areaIds;
    private List<Long> sectorIds;

    @Builder
    public PostSearch(String areaIds, String sectorIds) {
        this.areaIds = makeUniqueIds("areaID", areaIds);
        this.sectorIds = makeUniqueIds("sectorId", sectorIds);
    }

    private List<Long> makeUniqueIds(String name, String ids) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return Arrays.stream(ids.split(DELIMITER))
                .map(Long::valueOf)
                .distinct()
                .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            throw new WrongUserInputException(name + "필터에 올바르지 않은 Id가 입력되었습니다.");
        }
    }

    public boolean isAreaFilter() {
        return !areaIds.isEmpty() && sectorIds.isEmpty();
    }

    public boolean isSectorFilter() {
        return areaIds.isEmpty() && !sectorIds.isEmpty();
    }

    public boolean isNoFilter() {
        return areaIds.isEmpty() && sectorIds.isEmpty();
    }
}
