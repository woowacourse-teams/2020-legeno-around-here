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

    private final Long areaId;
    private final List<Long> sectorIds;

    @Builder
    public PostSearch(Long areaId, String sectorIds) {
        this.areaId = areaId;
        this.sectorIds = makeSectorIds(sectorIds);
    }

    private List<Long> makeSectorIds(String sectorIds) {
        if (Objects.isNull(sectorIds) || sectorIds.isEmpty()) {
            return new ArrayList<>();
        }
        return splitSectorIds(sectorIds);
    }

    private List<Long> splitSectorIds(String sectorIds) {
        try {
            return Arrays.stream(sectorIds.split(DELIMITER))
                .map(Long::valueOf)
                .distinct()
                .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            throw new WrongUserInputException("sectorIds 필터에 올바르지 않은 Id가 입력되었습니다.");
        }
    }

    public boolean isAreaFilter() {
        return Objects.nonNull(areaId) && sectorIds.isEmpty();
    }

    public boolean isSectorFilter() {
        return Objects.isNull(areaId) && !sectorIds.isEmpty();
    }

    public boolean isNotExistsFilter() {
        return Objects.isNull(areaId) && sectorIds.isEmpty();
    }
}
