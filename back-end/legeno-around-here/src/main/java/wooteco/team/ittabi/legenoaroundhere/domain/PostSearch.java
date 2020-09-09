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

    private static final String DELIMITER = ",";

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
            throw new WrongUserInputException("존재하지 않는 부문입니다.");
        }
    }

    public boolean isAreaFilter() {
        return Objects.nonNull(this.areaId) && this.sectorIds.isEmpty();
    }

    public boolean isSectorFilter() {
        return Objects.isNull(this.areaId) && !this.sectorIds.isEmpty();
    }

    public boolean isNotExistsFilter() {
        return Objects.isNull(this.areaId) && this.sectorIds.isEmpty();
    }
}
